/*
 * Copyright 2024 HM Revenue & Customs
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

import com.blibli.oss.qa.util.services.NetworkListener
import org.scalatest.{Documenting, Outcome, TestSuite, TestSuiteMixin}
import uk.gov.hmrc.selenium.webdriver.Driver
import org.openqa.selenium.io.FileHandler.createDir
import org.openqa.selenium.remote.Augmenter

trait HARRecording extends TestSuiteMixin with Documenting { this: TestSuite =>

  abstract override def withFixture(test: NoArgTest): Outcome = {
    val testName        = test.name.replaceAll(" ", "-").replaceAll(":", "")
    val outputDir       = "./target/test-reports/har-recordings"
    createDir(new java.io.File(outputDir))
    val networkListener = new NetworkListener(new Augmenter().augment(Driver.instance), s"$outputDir/$testName.har")
    networkListener.start()
    val testOutcome     = super.withFixture(test)
    networkListener.createHarFile()
    testOutcome
  }

}
