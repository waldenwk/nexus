# Nexus 平台完整部署方案

## 1. 概述

Nexus 是一个基于微服务架构的校园社交网络平台，使用 Docker 和 Docker Compose 进行容器化部署。整个系统分为三个主要的 Docker Compose 配置文件：

1. `docker-compose.yml` - 基础设施服务（数据库、缓存、搜索引擎）
2. `docker-compose-services.yml` - 核心微服务（用户、内容、消息等服务）
3. `monitoring/docker-compose-monitoring.yml` - 监控服务（Prometheus、Grafana）

关于Kafka和MinIO的详细集成信息，请参考[Kafka和MinIO集成部署文档](DEPLOYMENT_KAFKA_MINIO.md)。

## 2. 部署架构

### 2.1 基础设施层
- **MySQL 8.0** - 主数据库
- **Redis** - 缓存和会话存储
- **Elasticsearch 7.17.0** - 搜索引擎
- **Kibana 7.17.0** - 日志可视化
- **MinIO** - 对象存储服务
- **Apache Kafka** - 消息队列
- **ZooKeeper** - Kafka 协调服务

### 2.2 微服务层
- **Eureka Server** - 服务注册与发现
- **API Gateway** - API 网关
- **Auth Service** - 认证服务
- **User Service** - 用户服务
- **Content Service** - 内容服务
- **Message Service** - 消息服务
- **Feed Service** - 信息流服务
- **Group Service** - 群组服务
- **Event Service** - 活动服务
- **Search Service** - 搜索服务
- **File Service** - 文件服务
- **Recommendation Service** - 推荐服务
- **Frontend** - 前端应用

### 2.3 监控层
- **Prometheus** - 监控和告警
- **Grafana** - 数据可视化

## 3. 部署前准备

### 3.1 系统要求
- Docker 20.10+
- Docker Compose 1.29+
- 至少 8GB RAM
- 至少 20GB 磁盘空间

### 3.2 环境变量配置
在部署之前，请检查并根据需要修改以下配置文件中的环境变量：
- `docker-compose.yml` - 数据库和缓存配置
- `docker-compose-services.yml` - 各微服务配置

## 4. 部署步骤

### 4.1 克隆代码库
```bash
git clone <repository-url>
cd nexus
```

### 4.2 构建和启动基础设施
```bash
docker-compose up -d
```

等待基础设施服务完全启动（约2-3分钟）。

### 4.3 构建和启动微服务
```bash
docker-compose -f docker-compose-services.yml up -d
```

### 4.4 启动监控服务（可选）
```bash
cd monitoring
docker-compose -f docker-compose-monitoring.yml up -d
```

### 4.5 验证部署
1. 访问 Eureka 控制台: http://localhost:8761
2. 访问 API 网关: http://localhost:8080
3. 访问前端应用: http://localhost
4. 访问监控面板（如果启动）: http://localhost:3000

## 5. 服务访问地址

| 服务 | 地址 | 端口 |
|------|------|------|
| 前端应用 | http://localhost | 80 |
| API 网关 | http://localhost:8080 | 8080 |
| Eureka | http://localhost:8761 | 8761 |
| MySQL | localhost | 3306 |
| Redis | localhost | 6379 |
| Elasticsearch | http://localhost:9200 | 9200 |
| Kibana | http://localhost:5601 | 5601 |
| MinIO | http://localhost:9000 | 9000/9001 |
| Kafka | localhost | 9092 |
| Prometheus | http://localhost:9090 | 9090 |
| Grafana | http://localhost:3000 | 3000 |

## 6. 数据持久化

以下数据卷用于持久化存储：

- `mysql_data` - MySQL 数据
- `redis_data` - Redis 数据
- `elasticsearch_data` - Elasticsearch 数据
- `minio_data` - MinIO 对象存储数据

这些数据卷会在容器删除后仍然保留数据。

## 7. 监控和日志

### 7.1 Prometheus 监控
Prometheus 会自动抓取所有带有 `/actuator/prometheus` 端点的 Spring Boot 服务的指标。同时配置了丰富的告警规则，包括服务可用性、CPU/内存使用率、GC频率、HTTP错误率等关键指标监控。Prometheus还监控基础设施组件如MySQL和Redis的性能和可用性。

### 7.2 Grafana 可视化
Grafana 预配置了 Prometheus 数据源和预定义仪表板，包括Spring Boot统计信息和HTTP统计信息仪表板，提供开箱即用的监控体验。用户可以通过这些仪表板实时查看系统性能和健康状况。

### 7.3 监控指标
系统提供以下监控指标：

1. **JVM性能指标**
   - 内存使用情况（堆内存、非堆内存、各代内存）
   - GC活动（GC次数、GC时间）
   - 线程状态（活跃线程数、守护线程数等）
   - 类加载信息

2. **HTTP请求指标**
   - 请求速率和吞吐量
   - 响应时间分布
   - HTTP状态码统计
   - 错误率监控

3. **数据库指标**
   - 连接池状态（活跃连接数、等待连接数）
   - 查询性能统计
   - 连接使用率

4. **Kafka指标**
   - 消息生产消费速率
   - 消费者组延迟
   - 主题消息积压情况

5. **业务指标**
   - 各服务核心业务执行情况
   - 用户活动统计
   - 系统负载指标

### 7.4 告警规则
系统预配置了以下告警规则：

1. **服务可用性告警**：服务实例宕机时触发
2. **资源使用率告警**：CPU、内存使用率过高时触发
3. **性能告警**：GC频率过高、HTTP错误率过高时触发
4. **组件状态告警**：数据库连接池使用率过高、Kafka消息积压时触发

### 7.5 自定义监控
用户可以根据需要：
1. 在Prometheus中添加自定义查询规则
2. 在Grafana中创建自定义仪表板
3. 添加新的告警规则以满足特定需求

### 7.3 日志查看
使用以下命令查看特定服务的日志：
```bash
docker-compose logs -f <service-name>
```

## 8. 故障排除

### 8.1 服务启动失败
1. 检查 Docker 资源分配是否足够
2. 查看服务日志: `docker-compose logs <service-name>`
3. 确保端口未被占用

### 8.2 数据库连接问题
1. 验证数据库服务是否正常运行
2. 检查环境变量配置是否正确
3. 确认网络连接是否正常

### 8.3 服务间通信问题
1. 检查 Eureka 服务注册状态
2. 验证服务间网络连接
3. 确认配置文件中的服务地址是否正确

### 8.4 Kafka 和 MinIO 相关问题
详细信息请参考[Kafka和MinIO集成部署文档](DEPLOYMENT_KAFKA_MINIO.md)。

1. 检查 Kafka 和 ZooKeeper 是否正常运行
2. 验证 MinIO 是否正常启动并能访问控制台
3. 确认相关服务的环境变量配置是否正确

## 9. 扩展部署

### 9.1 水平扩展
可以通过修改 `docker-compose-services.yml` 文件中的 `replicas` 参数来扩展服务实例：
```yaml
deploy:
  replicas: 3
```

### 9.2 负载均衡
在生产环境中，建议在前端应用前部署 NGINX 或其他负载均衡器。

### 9.3 SSL 终止
在生产环境中，应在负载均衡器或反向代理层配置 SSL 证书。

## 10. 备份和恢复

### 10.1 数据备份
定期备份以下数据卷：
```bash
docker run --rm -v mysql_data:/data -v $(pwd):/backup alpine tar czf /backup/mysql-backup.tar.gz -C /data .
```

### 10.2 数据恢复
```
docker run --rm -v mysql_data:/data -v $(pwd):/backup alpine tar xzf /backup/mysql-backup.tar.gz -C /data
```

## 11. 更新和维护

### 11.1 代码更新
1. 拉取最新代码: `git pull`
2. 重新构建服务: `docker-compose build`
3. 重启服务: `docker-compose up -d`

### 11.2 数据库迁移
数据库迁移脚本应放在 `mysql/init/` 目录下，会在数据库首次启动时自动执行。

## 12. 安全考虑

1. 修改默认密码和密钥
2. 配置防火墙规则，限制外部访问
3. 定期更新 Docker 镜像
4. 使用 HTTPS 加密传输
5. 实施适当的访问控制策略