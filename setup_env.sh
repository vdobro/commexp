#!/bin/sh
if [ -z "$DB_HOST" ]
then
  echo "\$DB_HOST must not be empty and must be defined as a valid host of a running Postgres instance."
  exit
fi
if [ -z "$DB_USERNAME" ]
then
  echo "\$DB_USERNAME must not be empty."
  exit
fi
if [ -z "$DB_PASSWORD" ]
then
  echo "\$DB_PASSWORD must not be empty."
  exit
fi
if [ -z "$DB_NAME" ]
then
  echo "\$DB_NAME must not be empty."
  exit
fi
if [ -z "$REDIS_HOST" ]
then
  echo "\$REDIS_HOST must not be empty with enabled caching."
  exit
fi
if [ -z "$APP_HOST" ]
then
  echo "\$APP_HOST is empty, the default host http://localhost:8080 will be used instead."
fi
if [ -z "$DATA_PROFILES" ]
then
  echo "\$DATA_PROFILES not set, sample data will not be imported."
fi

PATH_TO_ENV_JS=BOOT-INF/classes/static/assets
envsubst < $PATH_TO_ENV_JS/env.template.js > $PATH_TO_ENV_JS/env.js
java org.springframework.boot.loader.JarLauncher
