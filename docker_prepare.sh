#!/bin/sh

rm -rf src/main/resources/static/* && \
mvn clean package -DskipTests -Pproduction
