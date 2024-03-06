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

import org.openqa.selenium.devtools.{HasDevTools, NetworkInterceptor}
import org.openqa.selenium.remote.Augmenter
import org.openqa.selenium.remote.http.{Contents, HttpResponse, Route}
import uk.gov.hmrc.configuration.TestEnvironment
import uk.gov.hmrc.selenium.webdriver.Driver

class InterceptNetworkRequestSpec extends BaseSpec {

  Feature("Intercept network requests") {

    Scenario("intercept what would be a 404 and send a response instead") {
      val driver               = new Augmenter().augment(Driver.instance)
      val pageThatDoesNotExist = TestEnvironment.url("vat-flat-rate-calculator-frontend") + "/404"

      Given("page that does not exist")
      driver.get(pageThatDoesNotExist)
      driver.getPageSource should include("This page can’t be found")

      And("I intercept requests to that page and return 'Hello, World!'")
      val devTools    = driver.asInstanceOf[HasDevTools].getDevTools
      devTools.createSession()
      val interceptor = new NetworkInterceptor(
        driver,
        Route
          .matching(req => req.getUri.endsWith("/404"))
          .to(() => req => new HttpResponse().setContent(Contents.utf8String("Hello, World!")))
      )

      When("I revisit the intercepted page")
      driver.get(pageThatDoesNotExist)

      Then("I see 'Hello, World!'")
      println(driver.getPageSource)
      driver.getPageSource should include("Hello, World!")

      interceptor.close()
      devTools.close()
    }

  }

}
