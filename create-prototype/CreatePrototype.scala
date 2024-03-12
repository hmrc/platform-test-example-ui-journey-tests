//> using scala 3
//> using toolkit default
//> using dep "com.lihaoyi::pprint:0.8.1"
//> using dep "com.microsoft.playwright:playwright:1.42.0"
//> using dep "com.deque.html.axe-core:playwright:4.8.2"
//> using dep "nu.validator:validator:20.7.2"
//> using dep "org.jsoup:jsoup:1.17.2"

import com.deque.html.axecore.playwright.{AxeBuilder, Reporter}
import com.deque.html.axecore.results.AxeResults
import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import java.io.ByteArrayInputStream
import java.io.InputStream
import nu.validator.client.EmbeddedValidator
import org.jsoup.Jsoup

object VNU:
  private val validator: EmbeddedValidator = new nu.validator.client.EmbeddedValidator()
  def validate(input: String): String      = validate(new ByteArrayInputStream(input.getBytes("UTF-8")))
  def validate(input: InputStream): String = validator.validate(input)

@main def main(): Unit =
  val path = os.pwd / os.up / "target" / "test-reports" / "har-recordings"
  val hars = os.list(path).filter(_.ext == "har")
  val firstHar = hars.head
  val firstHarJson = ujson.read(os.read(firstHar))
  val htmlPageEntries = firstHarJson("log")("entries").arr.filter(entry =>
    entry("response")("headers").arr.exists(header =>
      header("name").str == "Content-Type" &&
        header("value").str.startsWith("text/html")
    ))
  val htmlPages = htmlPageEntries.sortBy(_("startedDateTime").str)
  val htmlPageUrls = htmlPages.map(_("request")("url"))
  val playwright = Playwright.create()
  val browser = playwright.chromium().launch()
  val context = browser.newContext()
  val page = context.newPage()
  page.routeFromHAR(firstHar.wrapped)
  for((url, i) <- htmlPageUrls.zipWithIndex) do
    page.navigate(url.str)
    page.screenshot(new Page.ScreenshotOptions()
      .setPath(firstHar.wrapped.resolveSibling(s"${firstHar.wrapped.getFileName}-page-$i.png"))
      .setFullPage(true))
    val axeResults: AxeResults = new AxeBuilder(page).analyze()
    os.remove(os.Path(firstHar.wrapped.resolveSibling(s"${firstHar.wrapped.getFileName}-page-$i.axe-results.json")))
    new Reporter().JSONStringify(axeResults, firstHar.wrapped.resolveSibling(s"${firstHar.wrapped.getFileName}-page-$i.axe-results.json").toString)
    os.write.over(
      os.Path(firstHar.wrapped.resolveSibling(s"${firstHar.wrapped.getFileName}-page-$i.vnu-results.json")),
      VNU.validate(htmlPages.arr(i)("response")("content")("text").str)
    )
    val prototypeKit = os.Path(firstHar.wrapped.resolveSibling(s"${firstHar.wrapped.getFileName}-prototype-kit"))
    val pageRelativeUrl = new java.net.URL(url.str).getPath
    val nextPageUrl = htmlPageUrls.arr.lift(i + 1).map(url => new java.net.URL(url.str).getPath)
    val pagePath = prototypeKit / os.RelPath(pageRelativeUrl.tail)
    os.makeDir.all(pagePath)
    val mainElement = Jsoup.parse(htmlPages.arr(i)("response")("content")("text").str).select("main.govuk-main-wrapper")
    if (nextPageUrl.isDefined) {
      println(s"updating form action from ${pageRelativeUrl} to ${nextPageUrl.get}")
      mainElement.select("form").attr("action", nextPageUrl.get)
    }
    os.write.over(
      pagePath / "index.njk",
      s"""
        |{% extends "layouts/main.html" %}
        |
        |{% block content %}
        |${mainElement.html()}
        |{% endblock %}
        |""".stripMargin
    )
  println("Creating a prototype from a HAR")

// cp -r ../target/test-reports/har-recordings/Feature-Check-VAT-flat-rate-Scenario-User-pays-annually-and-is-a-limited-cost-business.har-prototype-kit/* ./prototype-kit-example/app/views/
