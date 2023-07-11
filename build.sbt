lazy val root = (project in file("."))
  .enablePlugins(TestReportPlugin)
  .settings(
    name := "platform-test-example-ui-journey-tests",
    version := "0.1.0",
    scalaVersion := "2.13.8",
    libraryDependencies ++= Dependencies.test
  )
