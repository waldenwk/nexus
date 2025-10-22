# Nexus - 新一代校园社交网络平台

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)](BUILD)
[![Docker](https://img.shields.io/badge/docker%20build-automated-007ec6.svg)](DOCKER)

## 项目介绍

Nexus 是一个现代化的校园社交网络平台，复刻经典人人网（校内网）的校园实名社交精髓，同时融入 Facebook 全球验证的高粘性功能。平台采用先进的微服务架构和主流技术栈，确保系统的高可用、高并发和可扩展性。

### 项目特点

- **现代化架构**: 基于 Spring Cloud 的微服务架构，服务独立部署和扩展
- **高可用性**: 通过服务注册发现、负载均衡和熔断机制保障系统稳定性
- **高性能**: Redis 缓存、Elasticsearch 搜索优化系统性能
- **智能推荐**: 基于用户行为和关系的智能推荐算法
- **易部署**: 基于 Docker 容器化部署，一键启动整个平台
- **可监控**: 集成 Prometheus 和 Grafana 实现系统监控和告警

## 技术架构

- **后端框架**: Spring Boot, Spring Cloud Alibaba
- **服务注册/发现**: Eureka
- **API网关**: Spring Cloud Gateway
- **数据库**: MySQL 8.0
- **缓存**: Redis
- **搜索**: Elasticsearch
- **消息队列**: Apache Kafka
- **文件存储**: MinIO / AWS S3
- **认证授权**: Spring Security + JWT
- **监控**: Prometheus + Grafana

### 架构图

```
graph TB
    A[客户端] --> B[API网关]
    B --> C[Eureka注册中心]
    C --> D[用户服务]
    C --> E[内容服务]
    C --> F[消息服务]
    C --> G[信息流服务]
    C --> H[群组服务]
    C --> I[活动服务]
    C --> J[搜索服务]
    C --> K[文件服务]
    C --> L[认证服务]
    C --> M[推荐服务]
    
    D --> N[(MySQL)]
    E --> N
    F --> N
    G --> N
    G --> O[(Redis)]
    H --> N
    I --> N
    J --> P[(Elasticsearch)]
    K --> Q[(MinIO)]
    F --> R[(Kafka)]
    M --> R
    M --> O
    
    S[Prometheus] --> T[Grafana]
    S --> D
    S --> E
    S --> F
    S --> G
    S --> H
    S --> I
    S --> J
    S --> K
    S --> L
    S --> M
    S --> B
    S --> C
```

### 服务依赖关系

- 所有业务服务依赖 Eureka 进行服务注册与发现
- 用户服务、内容服务、消息服务等依赖 MySQL 进行数据存储
- 信息流服务和推荐服务额外依赖 Redis 进行缓存
- 搜索服务依赖 Elasticsearch 提供搜索功能
- 文件服务依赖 MinIO 提供分布式文件存储
- 消息服务和推荐服务依赖 Kafka 提供异步消息处理
- API网关负责路由所有外部请求到相应的服务

## 服务模块

1. **eureka-server**: 服务注册与发现中心
2. **api-gateway**: API网关
3. **user-service**: 用户服务（注册、登录、个人资料、好友关系）
4. **content-service**: 内容服务（动态、日志、相册、评论、点赞）
5. **message-service**: 消息服务（私信、系统通知）
6. **feed-service**: 信息流服务（聚合内容、智能排序）
7. **group-service**: 群组服务（群组管理、群组动态）
8. **event-service**: 活动服务（活动管理、参与状态）
9. **search-service**: 搜索服务（全局搜索）
10. **file-service**: 文件服务（文件上传、下载、处理）
11. **auth-service**: 认证服务（JWT Token生成与验证）
12. **recommendation-service**: 推荐服务（好友推荐、内容推荐、智能推送）

## 核心功能

### 人人网核心功能（80%相似度）

1. **实名认证与学校体系**
   - 用户注册时必须使用真实姓名和所属学校/院系/入学年份
   - 个人主页背景为"我的大学"，自动关联同学、校友

2. **个人主页（时光轴）**
   - 展示用户自己发布的所有状态、照片、日志的动态流
   - 可设置主页的公开程度（对所有人、仅同学、仅好友等）

3. **好友系统**
   - 发送、接受、拒绝好友请求
   - 好友列表与分组管理

4. **状态/日志/相册**
   - 发布纯文本状态、图文状态
   - 撰写长篇博客（日志）
   - 创建相册、上传照片、对照片进行圈人（好友标签）

5. **站内信（私信）**
   - 用户与好友之间的一对一实时聊天

6. **留言板（墙）**
   - 在好友的个人主页留言，留言对所有访问该主页的人可见

7. **公共主页/群组**
   - 按学校、院系、班级建立公共群组，用户可加入并讨论

### Facebook高使用率功能

1. **"点赞"与"心情"反应**
   - 超越简单的"赞"，提供"哈哈"、"心"、"哇"、"难过"、"怒"等反应

2. **智能信息流**
   - 不再是单纯的时间线，而是基于算法排序。排序因素包括：好友亲密度、内容互动率（点赞评论数）、内容类型偏好等

3. **"故事"功能**
   - 发布24小时后消失的短视频或图片，在信息流顶部显示

4. **活动/事件管理**
   - 创建线上/线下活动，邀请好友，设置公开/私密属性，用户可回复"参加"、"可能参加"、"不参加"

5. **更强大的群组功能**
   - 支持公开、封闭、秘密三种群组类型
   - 丰富的群组管理工具（管理员、审核入群、发布公告等）

6. **页面**
   - 为校园社团、学生组织、本地商家创建官方页面，用户可"关注"而非"加为好友"

## 部署说明

### 前置条件

1. 安装 Docker 和 Docker Compose
2. 确保系统有至少 8GB 内存
3. 开放所需端口（参考端口分配表）
4. 确保系统有至少 20GB 可用磁盘空间

### 系统要求

- **操作系统**: Linux (推荐 Ubuntu 20.04+/CentOS 8+) / macOS / Windows 10+
- **内存**: 最低 8GB，推荐 16GB
- **Docker**: 20.10+
- **Docker Compose**: 1.29+

### 基础设施部署

使用Docker Compose部署基础设施：

```bash
docker-compose up -d
```

这将启动MySQL、Redis、Elasticsearch、Kafka、MinIO等基础设施服务。

### 监控系统部署

```bash
cd monitoring
docker-compose -f docker-compose-monitoring.yml up -d
```

这将启动Prometheus和Grafana监控系统。

### 服务部署

```bash
docker-compose -f docker-compose-services.yml up -d
```

这将启动所有微服务。

### 完整部署流程

1. 克隆项目代码：
   ```bash
   git clone <项目地址>
   cd nexus
   ```

2. 启动基础设施服务：
   ```bash
   docker-compose up -d
   ```

3. 等待基础设施启动完成（约1-2分钟），检查各组件是否正常运行：
   ```bash
   docker-compose ps
   ```

4. 启动监控系统（可选）：
   ```bash
   cd monitoring
   docker-compose -f docker-compose-monitoring.yml up -d
   cd ..
   ```

5. 启动业务服务：
   ```bash
   docker-compose -f docker-compose-services.yml up -d
   ```

6. 检查所有服务是否正常运行：
   ```bash
   docker-compose -f docker-compose-services.yml ps
   ```

7. 访问 Eureka 控制台确认所有服务注册成功：
   http://localhost:8761

### 部署验证

部署完成后，可以通过以下方式验证系统是否正常运行：

1. **服务健康检查**
   - 检查 Eureka 控制台中所有服务状态为 UP
   - 访问各服务的健康检查端点，如 http://localhost:8081/actuator/health

2. **基础功能测试**
   - 注册新用户: `POST /api/auth/signup`
   - 用户登录: `POST /api/auth/signin`
   - 获取用户信息: `GET /api/user/profile`

3. **数据库验证**
   - 连接 MySQL 数据库，检查 nexus 数据库及表结构是否创建成功
   - 验证初始数据是否正确插入

4. **文件存储验证**
   - 上传文件到文件服务，验证文件是否存储在MinIO中
   - 通过MinIO控制台查看存储的文件

5. **消息队列验证**
   - 发送消息，验证Kafka是否正常处理消息
   - 通过Kafka工具查看消息是否正确投递

## 数据库配置

所有服务默认连接本地 MySQL 数据库，数据库名为 `nexus`，用户名和密码均为 `root`。

### 数据库初始化

系统启动时会自动创建数据库表结构，初始化脚本位于 [mysql/init/01-init.sql](mysql/init/01-init.sql)。

如需手动初始化，请执行：

```sql
CREATE DATABASE IF NOT EXISTS nexus DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

数据库包含以下核心表：
- `users`: 用户基本信息表
- `user_relationship`: 用户关系表
- `posts`: 内容发布表
- `albums`: 相册表
- `photos`: 照片表
- `comments`: 评论表
- `likes`: 点赞表
- `private_messages`: 私信表
- `groups`: 群组表
- `group_members`: 群组成员表
- `events`: 活动表
- `event_attendees`: 活动参与表

## 端口分配

| 服务名称 | 端口 | 说明 |
|---------|------|------|
| eureka-server | 8761 | 服务注册中心 |
| api-gateway | 8080 | API网关 |
| user-service | 8081 | 用户服务 |
| content-service | 8082 | 内容服务 |
| message-service | 8083 | 消息服务 |
| feed-service | 8084 | 信息流服务 |
| group-service | 8085 | 群组服务 |
| event-service | 8086 | 活动服务 |
| search-service | 8087 | 搜索服务 |
| file-service | 8088 | 文件服务 |
| auth-service | 8089 | 认证服务 |
| recommendation-service | 8090 | 推荐服务 |
| mysql | 3306 | 数据库 |
| redis | 6379 | 缓存 |
| elasticsearch | 9200/9300 | 搜索引擎 |
| kibana | 5601 | 日志可视化 |
| prometheus | 9090 | 监控系统 |
| grafana | 3000 | 监控可视化 |
| minio | 9000/9001 | 对象存储服务/API和控制台 |
| kafka | 9092 | 消息队列 |
| zookeeper | 2181 | Kafka依赖的协调服务 |

## 认证流程

1. 用户通过 `/api/auth/signup` 注册账号
2. 用户通过 `/api/auth/signin` 登录获取 JWT Token
3. 后续请求需在 Header 中添加 `Authorization: Bearer <token>` 进行身份验证

## API 文档

API 文档使用 Swagger 生成，各服务的文档地址如下：

- 用户服务: http://localhost:8081/swagger-ui.html
- 内容服务: http://localhost:8082/swagger-ui.html
- 消息服务: http://localhost:8083/swagger-ui.html
- 信息流服务: http://localhost:8084/swagger-ui.html
- 群组服务: http://localhost:8085/swagger-ui.html
- 活动服务: http://localhost:8086/swagger-ui.html
- 搜索服务: http://localhost:8087/swagger-ui.html
- 文件服务: http://localhost:8088/swagger-ui.html
- 认证服务: http://localhost:8089/swagger-ui.html

通过 API 网关访问: http://localhost:8080/api/{service-name}/swagger-ui.html

## 监控面板

- **Eureka**: http://localhost:8761
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000 (默认用户名/密码: admin/admin)

## 故障排除

### 常见问题

1. **服务启动失败**
   - 检查端口是否被占用
   - 确认基础设施服务已正常启动
   - 查看服务日志: `docker-compose logs <service-name>`

2. **数据库连接失败**
   - 检查 MySQL 服务是否正常运行
   - 确认数据库配置是否正确
   - 检查防火墙设置

3. **服务间调用失败**
   - 检查 Eureka 控制台确认服务是否注册成功
   - 检查网络连接是否正常

4. **监控面板无法访问**
   - 检查 Prometheus 和 Grafana 服务是否正常启动
   - 确认端口映射是否正确
   - 检查防火墙设置

5. **搜索功能异常**
   - 检查 Elasticsearch 服务是否正常运行
   - 确认索引是否创建成功
   - 检查服务配置是否正确

6. **文件上传失败**
   - 检查 MinIO 服务是否正常运行
   - 确认 MinIO 配置是否正确
   - 检查存储空间是否充足

7. **消息处理异常**
   - 检查 Kafka 服务是否正常运行
   - 确认 Kafka 配置是否正确
   - 检查消息是否积压

### 诊断工具

1. **Docker 相关命令**
   ```bash
   # 查看所有容器状态
   docker-compose ps
   
   # 查看容器资源使用情况
   docker stats
   
   # 进入容器内部调试
   docker exec -it <container-name> /bin/bash
   ```

2. **服务健康检查**
   ```bash
   # 检查服务健康状态
   curl http://localhost:<service-port>/actuator/health
   
   # 检查服务指标
   curl http://localhost:<service-port>/actuator/metrics
   ```

3. **日志分析**
   ```bash
   # 实时查看所有服务日志
   docker-compose logs -f
   
   # 查看特定服务日志
   docker-compose logs -f <service-name>
   
   # 查看最近100行日志
   docker-compose logs --tail=100 <service-name>
   ```

4. **MinIO诊断**
   ```bash
   # 访问MinIO控制台
   http://localhost:9001
   
   # 使用MinIO客户端检查存储桶
   mc ls local/
   ```

5. **Kafka诊断**
   ```bash
   # 查看Kafka主题列表
   docker exec nexus-kafka kafka-topics.sh --list --bootstrap-server localhost:9092
   
   # 查看特定主题详情
   docker exec nexus-kafka kafka-topics.sh --describe --topic <topic-name> --bootstrap-server localhost:9092
   ```

### 日志查看

查看所有服务日志：
```bash
docker-compose logs -f
```

查看特定服务日志：
```bash
docker-compose logs -f <service-name>
```

## 扩展与维护

### 水平扩展

各微服务均可独立水平扩展：

```bash
docker-compose -f docker-compose-services.yml up -d --scale user-service=3
```

Kafka和MinIO也支持集群部署以提高可用性和性能。

### 数据备份

定期备份重要数据：

```bash
docker exec nexus-mysql /usr/bin/mysqldump -u root -p nexus > backup.sql
```

MinIO中的文件数据也需要定期备份。

### 版本升级

1. 拉取最新代码
2. 停止当前服务
3. 重新构建镜像
4. 启动服务

```bash
docker-compose -f docker-compose-services.yml down
docker-compose -f docker-compose-services.yml build
docker-compose -f docker-compose-services.yml up -d
```

### 监控告警

系统通过 Prometheus 和 Grafana 提供全面的监控告警功能：

1. **服务监控**
   - JVM 性能指标
   - HTTP 请求延迟和吞吐量
   - 数据库连接池状态
   - 缓存命中率

2. **基础设施监控**
   - CPU、内存、磁盘使用率
   - 网络流量
   - 容器资源使用情况
   - Kafka消息队列状态
   - MinIO存储使用情况

3. **自定义告警规则**
   - 服务不可用告警
   - 性能阈值告警
   - 资源使用率告警
   - Kafka消息积压告警
   - MinIO存储空间告警

### 日志管理

系统集成了 ELK（Elasticsearch、Logstash、Kibana）进行日志管理：

1. **日志收集**
   - 所有服务日志统一收集
   - 结构化日志处理
   - 日志持久化存储

2. **日志分析**
   - 通过 Kibana 进行日志可视化分析
   - 实时日志监控
   - 异常日志告警

3. **日志查询**
   - 支持按时间、服务、关键字等条件查询
   - 支持日志聚合分析
   - 支持日志导出

### 安全加固

为确保系统安全，建议采取以下措施：

1. **访问控制**
   - 配置防火墙，仅开放必要端口
   - 使用 HTTPS 加密传输
   - 实施 JWT Token 过期和刷新机制

2. **数据安全**
   - 定期备份数据库
   - 敏感信息加密存储
   - 实施数据库访问控制
   - MinIO访问密钥安全管理

3. **网络安全**
   - 使用 VPN 访问管理接口
   - 配置 API 限流防止恶意请求
   - 定期更新系统和组件版本
   - Kafka和MinIO网络安全配置

## 未来发展规划

### 功能扩展路线图

1. **AI智能化功能**
   - 智能内容推荐系统
   - 用户行为分析和预测
   - 智能内容审核
   - 聊天机器人助手

2. **多媒体功能增强**
   - 视频直播和短视频功能
   - AR/VR社交体验
   - 语音消息和语音识别
   - 实时协作编辑

3. **全球化支持**
   - 多语言界面支持
   - 国际化内容适配
   - 跨时区事件管理
   - 多币种虚拟礼物系统

### 技术演进方向

1. **架构优化**
   - 服务网格集成（Istio）
   - 无服务器架构（Serverless）
   - 边缘计算节点部署
   - 多云部署支持

2. **运维智能化**
   - AIOPS 智能运维
   - 自动故障预测和恢复
   - 智能容量规划
   - 自动化安全检测

3. **新兴技术融合**
   - 区块链身份验证
   - 人工智能推荐算法
   - 物联网设备接入
   - 5G网络优化支持

## MinIO文件存储

Nexus平台使用MinIO作为分布式对象存储系统，用于存储用户上传的文件，包括图片、视频和其他附件。

### MinIO特性

- **高性能**: 提供高速的文件上传和下载
- **可扩展**: 支持水平扩展，满足不断增长的存储需求
- **兼容S3**: 兼容Amazon S3 API，便于集成和迁移
- **安全**: 支持访问控制和加密

### MinIO配置

MinIO在Docker Compose中配置如下：
- 端口: 9000 (API), 9001 (管理控制台)
- 默认访问密钥: minioadmin
- 默认秘密密钥: minioadmin123

### 使用说明

1. **访问控制台**
   通过 http://localhost:9001 访问MinIO管理控制台

2. **文件管理**
   - 文件按唯一标识符存储在存储桶中
   - 支持生成带时效性的访问链接
   - 支持文件删除操作

3. **集成方式**
   File Service通过MinIO Java SDK与MinIO服务交互，提供统一的文件操作接口

## Kafka消息队列

Nexus平台使用Apache Kafka作为消息队列系统，用于处理异步消息，包括私信通知、群组消息和系统通知等。

### Kafka特性

- **高吞吐量**: 支持大量消息的快速处理
- **持久化**: 消息持久化存储，确保不丢失
- **分布式**: 支持集群部署，提供高可用性
- **可扩展**: 支持水平扩展

### Kafka配置

Kafka在Docker Compose中配置如下：
- 端口: 9092
- 依赖ZooKeeper进行协调
- 自动创建主题功能已启用

### 使用说明

1. **主题管理**
   系统预定义了以下主题：
   - private-message: 私信消息
   - group-message: 群组消息
   - notification: 系统通知

2. **消息处理**
   - Message Service通过Kafka生产者发送消息
   - 消费者监听主题并处理消息
   - 支持消息的异步处理和解耦

3. **集成方式**
   Message Service通过Spring Kafka与Kafka服务交互，提供消息的生产和消费功能
