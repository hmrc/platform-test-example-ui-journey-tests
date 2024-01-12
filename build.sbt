lazy val root = (project in file("."))
  .settings(
    name := "platform-test-example-ui-journey-tests",
    version := "0.1.0",
    scalaVersion := "2.13.12",
    libraryDependencies ++= Dependencies.test
  )
