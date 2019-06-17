#!/usr/bin/env bash

ROOT_DIR=/home/hgc/wkspce/tallate/tianchi/adaptive-loadbalance

if [ $1 = 'install' ]; then
    cd dubbo-internal
    mvn clean install -Dmaven.test.skip=true
    cd ../internal-service/
    mvn clean install -Dmaven.test.skip=true
elif [ $1 = 'stop-0' ]; then
    docker stop consumer-default
    docker rm consumer-default
    docker stop provider-default-large
    docker rm provider-default-large
    docker stop provider-default-medium
    docker rm provider-default-medium
    docker stop provider-default-small
    docker rm provider-default-small
elif [ $1 = 'run-0' ]; then
    # 本地 Docker 跑官方版本
    cd ${ROOT_DIR}/localtest/debian-jdk8-adaptive-loadbalance/debian-jdk8-provider
    sudo docker build --build-arg user_code_address=https://code.aliyun.com/middlewarerace2019/adaptive-loadbalance.git -t provider-default .
    cd ${ROOT_DIR}/localtest/debian-jdk8-adaptive-loadbalance/debian-jdk8-consumer
    sudo docker build --build-arg user_code_address=https://code.aliyun.com/middlewarerace2019/adaptive-loadbalance.git -t consumer-default .
    docker run -td --network=host -p 20880:20880 --name=provider-default-small provider-default provider-small
    docker run -td --network=host -p 20880:20880 --name=provider-default-medium provider-default provider-medium
    docker run -td --network=host -p 20880:20880 --name=provider-default-large provider-default provider-large
    docker run -td --network=host -p 8087:8087 --name=consumer-default consumer-default
elif [ $1 = 'stop-1' ]; then
    docker stop consumer
    docker rm consumer
    docker stop provider-large
    docker rm provider-large
    docker stop provider-medium
    docker rm provider-medium
    docker stop provider-small
    docker rm provider-small
elif [ $1 = 'run-1' ]; then
    # 本地 Docker 跑一个
    # 构建最新的provider
    cd ${ROOT_DIR}/localtest/debian-jdk8-adaptive-loadbalance/debian-jdk8-provider
    sudo docker build --build-arg user_code_address=https://code.aliyun.com/787985369/adaptive-loadbalance.git -t provider .
    # 构建最新的consumer
    cd ${ROOT_DIR}/localtest/debian-jdk8-adaptive-loadbalance/debian-jdk8-consumer
    sudo docker build --build-arg user_code_address=https://code.aliyun.com/787985369/adaptive-loadbalance.git -t consumer .
    # 启动
    docker run -td --network=host -p 20880:20880 --name=provider-small provider provider-small
    docker run -td --network=host -p 20870:20870 --name=provider-medium provider provider-medium
    docker run -td --network=host -p 20890:20890 --name=provider-large provider provider-large
    docker run -td --network=host -p 8087:8087 --name=consumer consumer
elif [ $1 = 'warmup' ]; then
    # warmup
    cd ${ROOT_DIR}/localtest
    wrk -t2 -c512 -d30s -T5 --script=./wrk.lua --latency http://localhost:8087/invoke
elif [ $1 = 'press' ]; then
    # pressure test
    cd ${ROOT_DIR}/localtest
    wrk -t4 -c1024 -d60s -T5 --script=./wrk.lua --latency http://localhost:8087/invoke
fi


