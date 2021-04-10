#!/bin/sh

NAME="commexp"
VERSION="0.1-SNAPSHOT"

rm -rf src/main/resources/static
mkdir src/main/resources/static

mvn clean package

docker build -t dobrovolskis/$NAME:$VERSION .
docker push dobrovolskis/$NAME:$VERSION
