#!/usr/bin/env bash

cd dubbo-internal
mvn clean install -Dmaven.test.skip=true
cd ../internal-service/
mvn clean install -Dmaven.test.skip=true
