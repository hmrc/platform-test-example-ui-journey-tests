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

import uk.gov.hmrc.ui.pages.{CostOfGoods, Result, Turnover, VATReturnPeriod}
import uk.gov.hmrc.ui.specs.tags.ExampleTaggedTest
import org.scalatest.prop.TableDrivenPropertyChecks

class CheckVATFlatRateSpec extends BaseSpec with TableDrivenPropertyChecks {

  private val resultPage      = Result
  private val turnoverPage    = Turnover
  private val costOfGoodsPage = CostOfGoods
  private val periodPage      = VATReturnPeriod

  Feature("User should see correct VAT flat rate") {

    val vatFlatRateScenarios =
      Table(
        ("vatReturnPeriod", "turnover", "costOfGoods", "expectedOutcome"),
        ("annually", "1000", "999", resultPage.useSetVATFlatRate),
        ("annually", "1000", "1000", resultPage.useUniqueVATFlatRate),
        ("quarterly", "1000", "249", resultPage.useSetVATFlatRate),
        ("quarterly", "1000", "250", resultPage.useUniqueVATFlatRate)
      )

    forAll(vatFlatRateScenarios) {
      (vatReturnPeriod: String, turnover: String, costOfGoods: String, expectedOutcome: String) =>
        // This test is tagged with 'ExampleTaggedTest' to demonstrate how to use ScalaTest tags.
        // To run tagged tests only, use the sbt command shown in run-tests.sh.
        Scenario(
          s"User pays $vatReturnPeriod with turnover £$turnover and cost of goods £$costOfGoods",
          ExampleTaggedTest
        ) {

          Given(s"My VAT return period is $vatReturnPeriod")
          periodPage.goTo()
          periodPage.submit(vatReturnPeriod)

          And(s"My turnover for the period is £$turnover")
          turnoverPage.submit(turnover)

          When(s"My cost of goods for the period is £$costOfGoods")
          costOfGoodsPage.submit(costOfGoods)

          Then(s"The correct VAT flat rate is shown - $expectedOutcome")
          resultPage.outcome() should be(expectedOutcome)
        }
    }
  }
}
