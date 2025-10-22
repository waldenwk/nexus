#!/bin/bash

# 为所有微服务添加日志配置和Logstash依赖

SERVICES=("api-gateway" "auth-service" "content-service" "event-service" "feed-service" "file-service" "group-service" "message-service" "recommendation-service" "search-service" "user-service")

for service in "${SERVICES[@]}"; do
  echo "Processing $service..."
  
  # 创建logback-spring.xml配置文件
  mkdir -p "$service/src/main/resources"
  cp content-service/src/main/resources/logback-spring.xml "$service/src/main/resources/logback-spring.xml"
  
  # 更新pom.xml添加Logstash依赖
  if [ -f "$service/pom.xml" ]; then
    # 检查是否已存在Logstash依赖
    if ! grep -q "logstash-logback-encoder" "$service/pom.xml"; then
      # 在spring-boot-starter-test之前插入Logstash依赖
      sed -i '' '/<artifactId>spring-boot-starter-test<\/artifactId>/i\
        <!-- Logstash日志支持 -->\
        <dependency>\
            <groupId>net.logstash.logback<\/groupId>\
            <artifactId>logstash-logback-encoder<\/artifactId>\
            <version>7.2<\/version>\
        <\/dependency>\
        ' "$service/pom.xml"
    fi
  fi
  
  echo "Completed $service"
done

echo "All services processed!"