#!/usr/bin/env bash

if [  = '' ] then
    cd dubbo-internal
    mvn clean install -Dmaven.test.skip=true
    cd ../internal-service/
    mvn clean install -Dmaven.test.skip=true
elseif [] then

fi


