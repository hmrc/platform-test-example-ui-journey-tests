import sbt.*

object Dependencies {

  val test: Seq[ModuleID] = Seq(
    "ch.qos.logback"          % "logback-classic"        % "1.5.1"       % Test,
    "com.vladsch.flexmark"    % "flexmark-all"           % "0.64.8"      % Test,
    "org.scalatest"          %% "scalatest"              % "3.2.18"      % Test,
    "io.undertow"             % "undertow-core"          % "2.1.0.Final" % Test,
    "io.undertow"             % "undertow-servlet"       % "2.1.0.Final" % Test,
    "org.seleniumhq.selenium" % "selenium-devtools-v117" % "4.15.0"      % Test,
    "uk.gov.hmrc"            %% "ui-test-runner"         % "0.+"         % Test // Do NOT use .+ notation in test repositories
  )

}
