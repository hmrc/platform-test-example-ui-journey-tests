#!/bin/bash -e

BROWSER=$1
ENVIRONMENT=$2

sbt scalafmtCheckAll scalafmtSbtCheck

sbt -Dbrowser="${BROWSER:=chrome}" -Denvironment="${ENVIRONMENT:=local}" "testOnly uk.gov.hmrc.ui.specs.*"
