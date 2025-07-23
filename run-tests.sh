#!/usr/bin/env bash

BROWSER=$1
ENVIRONMENT=$2

sbt clean -Dbrowser="${BROWSER:=chrome}" -Denvironment="${ENVIRONMENT:=local}" -Dsecurity.assessment=false test testReport

# To run only tests tagged with 'ExampleTaggedTest', use the following sbt command:
#sbt clean -Dbrowser="${BROWSER:=chrome}" -Denvironment="${ENVIRONMENT:=local}" -Dsecurity.assessment=false "testOnly uk.gov.hmrc.ui.specs.* -- -n ExampleTaggedTest" testReport