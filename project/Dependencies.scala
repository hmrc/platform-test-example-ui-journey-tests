import sbt.*

object Dependencies {

  val test: Seq[ModuleID] = Seq(
    "com.vladsch.flexmark" % "flexmark-all"   % "0.62.2" % Test,
    "org.scalatest"       %% "scalatest"      % "3.2.18" % Test,
    "org.slf4j"            % "slf4j-simple"   % "1.7.36" % Test,
    "uk.gov.hmrc"         %% "ui-test-runner" % "0.+"    % Test // Do NOT use .+ notation in test repositories
  )

}
