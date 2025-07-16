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

package uk.gov.hmrc.ui.pages

import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import uk.gov.hmrc.configuration.TestEnvironment

object VATReturnPeriod extends BasePage {

  private val url: String = TestEnvironment.url("vat-flat-rate-calculator-frontend") + "/vat-return-period"

  private val annuallyRadioButton: By  = By.id("vatReturnPeriod")
  private val quarterlyRadioButton: By = By.id("vatReturnPeriod-2")

  def goTo(): Unit =
    get(url)

  def fluentGoTo(): Unit = {
    get(url)
    fluentWait.until(ExpectedConditions.urlContains(url))
  }

  def submit(value: String): Unit = {
    value match {
      case "annually"  => selectCheckbox(annuallyRadioButton)
      case "quarterly" => selectCheckbox(quarterlyRadioButton)
    }
    click(continueButton)
  }

}
