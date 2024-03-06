/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.ui.specs

import io.undertow.Undertow
import io.undertow.server.{HttpHandler, HttpServerExchange}
import io.undertow.util.{Headers, StatusCodes}
import org.openqa.selenium.devtools.NetworkInterceptor
import org.openqa.selenium.remote.Augmenter
import org.openqa.selenium.remote.http.{Contents, HttpResponse, Route}
import uk.gov.hmrc.configuration.TestEnvironment
import uk.gov.hmrc.selenium.webdriver.Driver

import scala.util.Using

class InterceptNetworkRequestSpec extends BaseSpec {

  Feature("Intercept network requests") {

    Scenario("intercept what would be a 404 and send a response instead") {
      val redirectionTarget     = TestEnvironment.url("vat-flat-rate-calculator-frontend")
      val redirectionServerPort = 6001 // must be a port mapped additionally by jenkins for this test as not via sm2
      val redirectionServerUrl  = s"http://localhost:$redirectionServerPort"
      val redirectionServer     = Undertow
        .builder()
        .addHttpListener(redirectionServerPort, "localhost")
        .setHandler(new HttpHandler() {
          override def handleRequest(exchange: HttpServerExchange): Unit = {
            exchange.setStatusCode(StatusCodes.FOUND);
            exchange.getResponseHeaders.put(Headers.LOCATION, redirectionTarget);
            exchange.endExchange()
          }
        })
        .build();
      redirectionServer.start();

      Given("requests are redirected")
      Driver.instance.get(redirectionServerUrl)
      Driver.instance.getCurrentUrl should be(redirectionTarget)
      Driver.instance.getPageSource should not include "blocked by selenium"

      And("I intercept redirection requests and return 'blocked by selenium'")
      def withBlockedUrl(blockedUrl: String)(useWebDriver: => Unit) =
        Using(
          new NetworkInterceptor(
            new Augmenter().augment(Driver.instance),
            Route
              .matching(req => req.getUri.equals(blockedUrl))
              .to(() => req => new HttpResponse().setContent(Contents.utf8String("blocked by selenium")))
          )
        )(_ => useWebDriver)

      When("I revisit the intercepted redirection url")
      withBlockedUrl(redirectionTarget) {
        Driver.instance.get(redirectionServerUrl)
      }

      Then("I see 'blocked by selenium'")
      Driver.instance.getCurrentUrl should be(redirectionTarget)
      Driver.instance.getPageSource should include("blocked by selenium")

      redirectionServer.stop()
    }

  }

}
