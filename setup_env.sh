#!/bin/sh
if [ -z "$DB_HOST" ]
then
  echo "\$DB_HOST may not be empty and must be defined as a valid host of a running Postgres instance."
  exit
fi
if [ -z "$DB_USERNAME" ]
then
  echo "\$DB_USERNAME may not be empty."
  exit
fi
if [ -z "$DB_PASSWORD" ]
then
  echo "\$DB_PASSWORD may not be empty."
  exit
fi
if [ -z "$DB_NAME" ]
then
  echo "\$DB_NAME may not be empty."
  exit
fi
if [ -z "$REDIS_HOST" ]
then
  echo "\$REDIS_HOST may not be empty with caching enabled."
  exit
fi
if [ -z "$DATA_PROFILES" ]
then
  echo "\$DATA_PROFILES not set, no sample data will be imported."
fi

java org.springframework.boot.loader.JarLauncher
