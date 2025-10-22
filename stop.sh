#!/bin/bash

# Nexus 平台停止脚本

echo "停止 Nexus 平台..."

echo "1. 停止监控服务..."
cd monitoring
docker-compose -f docker-compose-monitoring.yml down
cd ..

echo "2. 停止核心微服务..."
docker-compose -f docker-compose-services.yml down

echo "3. 停止基础设施服务..."
docker-compose down

echo "Nexus 平台已停止！"