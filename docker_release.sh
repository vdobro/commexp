#!/bin/sh

REPO="dobrovolskis"
NAME="commexp"
VERSION="0.1-SNAPSHOT"

#mvn spring-boot:build-image -Dspring-boot.build-image.imageName=$REPO/$NAME:$VERSION
docker build -t $REPO/$NAME:$VERSION -t $REPO/$NAME:latest . && \
docker push $REPO/$NAME:$VERSION && \
docker push $REPO/$NAME:latest
