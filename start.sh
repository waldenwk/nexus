#!/bin/bash

# Nexus 平台启动脚本

echo "启动 Nexus 平台..."

echo "1. 启动基础设施服务..."
docker-compose up -d

echo "2. 启动核心微服务..."
docker-compose -f docker-compose-services.yml up -d

echo "3. 启动监控服务..."
cd monitoring
docker-compose -f docker-compose-monitoring.yml up -d
cd ..

echo "Nexus 平台启动完成！"