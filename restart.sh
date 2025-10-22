#!/bin/bash

# Nexus 平台重启脚本

echo "重启 Nexus 平台..."

./stop.sh
sleep 10
./start.sh

echo "Nexus 平台重启完成！"