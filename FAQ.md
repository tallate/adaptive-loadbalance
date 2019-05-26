选手可以在 issue 或者钉钉群中提问，管理员会将有共性的问题整理于此

## 00. Docker provider 启动后立即退出/无法启动

1. 手动启动 provider container

    ```shell
    docker run -ti --entrypoint=/bin/bash -p 20880:20880  registry.cn-shanghai.aliyuncs.com/aliware2019/provider
    ```
2. 运行 `docker-entrypoint.sh`

    ```shell
    docker-entrypoint.sh provider-small &
    ```
3. 查看启动日志

    ```shell
    tail -f /root/runtime/logs/stdout.log /root/runtime/logs/service-provider.log
    ```

## 01 Docker consumer 启动后立即退出/无法启动

- 首先请确定已经在本地修改了 host 文件，添加了 provider 的ip。需要注意，provider 的 ip 需要使用外网/局域网的真实ip，不能使用 locahost 或 127.0.0.0 等环回地址。
- 默认需要启动三个 provider 容器，consumer才能正常启动，如果想只启动一个 provider，需要编辑 consumer 的`com.aliware.tianchi.netty.HttpProcessHandler#buildUrls`方法，将不需要的 provider url 注释掉。

1. 手动启动 consumer container

    ```shell
    docker run -ti --entrypoint=/bin/bash -p 8087:8087  registry.cn-shanghai.aliyuncs.com/aliware2019/consumer
    ```
2. 运行 `docker-entrypoint.sh`

    ```shell
    docker-entrypoint.sh  &
    ```
3. 查看启动日志

    ```shell
    tail -f /root/runtime/logs/stdout.log /root/runtime/logs/service-consumer.log
    ```
    如果有连接 provider 不成功的错误，一般是由于 provider 的 url 配置不对或 host 没配置，修改 localhost/127.0.0.1 等地址为真实ip即可修复