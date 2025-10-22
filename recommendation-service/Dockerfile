# 使用官方OpenJDK运行时作为基础镜像
FROM openjdk:11-jre-slim

# 设置维护者信息
LABEL maintainer="Nexus Team"

# 设置工作目录
WORKDIR /app

# 将打包好的jar文件复制到容器中
COPY target/recommendation-service-1.0.0.jar app.jar

# 暴露端口
EXPOSE 8090

# 运行jar文件
ENTRYPOINT ["java", "-jar", "app.jar"]