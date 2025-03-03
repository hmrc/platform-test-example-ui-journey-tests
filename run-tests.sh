#!/usr/bin/env bash

BROWSER=$1
ENVIRONMENT=$2
SE_BROWSER_MIRROR_URL=https://lab03.artefacts.tax.service.gov.uk/artifactory/storage-googleapis-com

sbt clean -Dbrowser="${BROWSER:=chrome}" -Denvironment="${ENVIRONMENT:=local}" -Dsecurity.assessment=false test testReport
