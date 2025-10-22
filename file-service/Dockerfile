# 使用官方 OpenJDK 镜像作为基础镜像
FROM openjdk:11-jre-slim

# 设置工作目录
WORKDIR /app

# 复制 JAR 文件到容器中
COPY target/file-service-1.0.0.jar app.jar

# 暴露端口
EXPOSE 8088

# 运行应用程序
ENTRYPOINT ["java", "-jar", "app.jar"]