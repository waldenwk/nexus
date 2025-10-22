#!/bin/bash

# Nexus 平台部署脚本

set -e  # 遇到错误时停止执行

echo "开始部署 Nexus 平台..."

# 检查 Docker 是否安装
if ! command -v docker &> /dev/null
then
    echo "错误: 未找到 Docker，请先安装 Docker"
    exit 1
fi

# 检查 Docker Compose 是否安装
if ! command -v docker-compose &> /dev/null
then
    echo "错误: 未找到 Docker Compose，请先安装 Docker Compose"
    exit 1
fi

echo "1. 启动基础设施服务..."
docker-compose up -d

echo "等待基础设施服务启动 (约60秒)..."
sleep 60

echo "2. 启动核心微服务..."
docker-compose -f docker-compose-services.yml up -d

echo "等待微服务启动 (约30秒)..."
sleep 30

echo "3. 启动监控服务..."
cd monitoring
docker-compose -f docker-compose-monitoring.yml up -d
cd ..

echo "部署完成！"
echo ""
echo "访问地址:"
echo "  前端应用: http://localhost"
echo "  Eureka 控制台: http://localhost:8761"
echo "  API 网关: http://localhost:8080"
echo "  Prometheus: http://localhost:9090"
echo "  Grafana: http://localhost:3000"
echo "  Kibana: http://localhost:5601"
echo ""
echo "查看服务状态:"
echo "  docker-compose ps"
echo "  docker-compose -f docker-compose-services.yml ps"
echo "  docker-compose -f monitoring/docker-compose-monitoring.yml ps"