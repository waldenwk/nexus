# Kafka和MinIO集成部署文档

## 概述

本文档详细描述了如何在Nexus社交平台中集成Apache Kafka消息队列和MinIO对象存储服务，以及相关的配置和部署说明。

## 1. Kafka集成改造

### 1.1 添加Kafka依赖

在message-service的`pom.xml`中添加了以下依赖：

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

### 1.2 Kafka配置

在message-service的`application.yml`中添加了Kafka配置：

```yaml
spring:
  kafka:
    bootstrap-servers: ${KAFKA_HOST:localhost}:${KAFKA_PORT:9092}
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
    consumer:
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      group-id: message-service-group
```

### 1.3 Kafka配置类

创建了`KafkaConfig`配置类，用于配置Kafka生产者和消费者工厂：

- `producerFactory()`: 创建Kafka生产者工厂
- `kafkaTemplate()`: 创建Kafka模板
- `consumerFactory()`: 创建Kafka消费者工厂
- `kafkaListenerContainerFactory()`: 创建Kafka监听器容器工厂

### 1.4 消息生产者和消费者

创建了两个核心类来处理Kafka消息：

1. `MessageProducer`: 负责发送消息到Kafka主题
   - `sendPrivateMessage()`: 发送私信消息
   - `sendGroupMessage()`: 发送群组消息
   - `sendNotification()`: 发送通知消息

2. `MessageConsumer`: 负责监听和处理来自Kafka主题的消息
   - `listenPrivateMessage()`: 监听私信消息
   - `listenGroupMessage()`: 监听群组消息
   - `listenNotification()`: 监听通知消息

### 1.5 服务集成

在`MessageService`中集成了Kafka消息发送功能，当有新消息发送时，同时通过Kafka发送通知。

## 2. MinIO集成改造

### 2.1 添加MinIO依赖

在file-service的`pom.xml`中添加了以下依赖：

```xml
<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
    <version>8.5.9</version>
</dependency>
```

### 2.2 MinIO配置

在file-service的`application.yml`中添加了MinIO配置：

```yaml
minio:
  endpoint: ${MINIO_ENDPOINT:http://localhost:9000}
  access-key: ${MINIO_ACCESS_KEY:minioadmin}
  secret-key: ${MINIO_SECRET_KEY:minioadmin}
  bucket: ${MINIO_BUCKET:nexus-files}
```

### 2.3 MinIO配置类

创建了`MinioConfig`配置类，用于创建MinioClient Bean：

```java
@Bean
public MinioClient minioClient() {
    return MinioClient.builder()
            .endpoint(endpoint)
            .credentials(accessKey, secretKey)
            .build();
}
```

### 2.4 文件服务改造

重写了`FileService`类，使用MinIO替代本地文件系统：

- `saveFile()`: 将文件保存到MinIO存储桶中
- `getFileUrl()`: 获取文件访问URL
- `getFile()`: 从MinIO获取文件数据
- `deleteFile()`: 从MinIO删除文件

## 3. Docker Compose配置

在`docker-compose-services.yml`中添加了Kafka和MinIO服务：

### 3.1 MinIO服务配置

```yaml
minio:
  image: minio/minio:RELEASE.2023-05-04T21-44-30Z
  container_name: nexus-minio
  ports:
    - "9000:9000"
    - "9001:9001"
  environment:
    MINIO_ACCESS_KEY: minioadmin
    MINIO_SECRET_KEY: minioadmin123
  command: server /data --console-address ":9001"
  healthcheck:
    test: ["CMD", "curl", "-f", "http://localhost:9000/minio/health/live"]
    interval: 30s
    timeout: 20s
    retries: 3
  volumes:
    - ./minio/data:/data
  networks:
    - nexus-network
```

### 3.2 Kafka服务配置

```yaml
kafka:
  image: bitnami/kafka:3.4.0
  container_name: nexus-kafka
  ports:
    - "9092:9092"
  environment:
    KAFKA_CFG_LISTENERS: PLAINTEXT://:9092
    KAFKA_CFG_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
    KAFKA_CFG_ZOOKEEPER_CONNECT: zookeeper:2181
    KAFKA_CFG_AUTO_CREATE_TOPICS_ENABLE: true
    KAFKA_CFG_NUM_PARTITIONS: 3
  depends_on:
    zookeeper:
      condition: service_healthy
  networks:
    - nexus-network

zookeeper:
  image: bitnami/zookeeper:3.8.0
  container_name: nexus-zookeeper
  ports:
    - "2181:2181"
  environment:
    ZOO_ENABLE_ADMIN_SERVER: "no"
  healthcheck:
    test: ["CMD", "zkServer.sh", "status"]
    interval: 10s
    timeout: 5s
    retries: 3
  networks:
    - nexus-network
```

### 3.3 服务依赖更新

更新了相关服务的依赖配置，确保它们能正确连接到Kafka和MinIO：

1. message-service依赖Kafka服务：
```yaml
depends_on:
  mysql:
    condition: service_healthy
  kafka:
    condition: service_started
  eureka-server:
    condition: service_healthy
```

2. file-service依赖MinIO服务：
```yaml
depends_on:
  minio:
    condition: service_healthy
  eureka-server:
    condition: service_healthy
```

## 4. 环境变量配置

### 4.1 Kafka环境变量

- `KAFKA_HOST`: Kafka服务器主机地址，默认为`localhost`
- `KAFKA_PORT`: Kafka服务器端口，默认为`9092`

### 4.2 MinIO环境变量

- `MINIO_ENDPOINT`: MinIO服务器地址，默认为`http://localhost:9000`
- `MINIO_ACCESS_KEY`: MinIO访问密钥，默认为`minioadmin`
- `MINIO_SECRET_KEY`: MinIO秘密密钥，默认为`minioadmin`
- `MINIO_BUCKET`: MinIO存储桶名称，默认为`nexus-files`

## 5. 使用说明

### 5.1 启动服务

使用以下命令启动所有服务，包括Kafka和MinIO：

```bash
docker-compose -f docker-compose-services.yml up -d
```

### 5.2 访问服务

- MinIO控制台: http://localhost:9001
- Kafka服务: localhost:9092

### 5.3 测试功能

1. 文件上传测试:
   - 通过API上传文件，文件将存储在MinIO中
   - 可以通过MinIO控制台查看存储的文件

2. 消息队列测试:
   - 发送私信、群组消息或通知
   - 观察Kafka消费者是否接收到相应的消息

## 6. 注意事项

1. 确保Docker有足够的资源运行所有服务
2. 在生产环境中，应使用更安全的密钥管理方式
3. 可以根据实际需求调整Kafka主题和分区数量
4. 建议为MinIO配置持久化存储路径

## 7. 推荐服务集成

### 7.1 推荐服务中的Kafka集成

推荐服务也集成了Kafka，用于监听用户行为事件以优化推荐算法：

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

### 7.2 推荐服务中的Redis集成

推荐服务使用Redis作为缓存，以提高推荐算法的性能：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### 7.3 推荐服务配置

在推荐服务的`application.yml`中配置了Redis和Kafka：

```yaml
spring:
  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
    timeout: 2000ms
  kafka:
    bootstrap-servers: ${KAFKA_HOST:localhost}:${KAFKA_PORT:9092}
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
    consumer:
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      group-id: recommendation-service-group
```

### 7.4 推荐服务功能

推荐服务提供以下核心功能：

1. **智能好友推荐**：
   - 基于同学关系（同一学校同年入学）
   - 基于同事关系（相同工作单位）
   - 基于共同好友
   - 基于兴趣爱好相似度
   - 基于地理位置接近度

2. **个性化内容推荐**：
   - 基于用户偏好
   - 基于好友活动
   - 基于内容相似度
   - 基于协同过滤算法

3. **实时推荐更新**：
   - 通过Kafka监听用户行为事件
   - 动态调整推荐算法参数
   - 实时更新用户偏好画像

4. **推荐效果评估**：
   - 记录推荐查看和接受情况
   - 分析推荐转化率
   - 持续优化推荐算法

### 7.5 监控集成

推荐服务与监控系统完全集成，提供以下监控指标：

1. **性能监控**：
   - JVM性能指标（内存、CPU、GC等）
   - HTTP请求延迟和吞吐量
   - Redis连接池状态
   - Kafka消费者组延迟

2. **业务监控**：
   - 推荐算法执行时间
   - 推荐结果生成数量
   - 用户对推荐的接受率
   - 推荐点击率和转化率
