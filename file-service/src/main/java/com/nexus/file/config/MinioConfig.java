package com.nexus.file.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO配置类
 */
@Configuration
public class MinioConfig {
    
    @Value("${minio.endpoint}")
    private String endpoint;
    
    @Value("${minio.access-key}")
    private String accessKey;
    
    @Value("${minio.secret-key}")
    private String secretKey;
    
    /**
     * 创建MinioClient Bean
     * @return MinioClient实例
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
    
    /**
     * 创建一个用于高并发场景的MinioClient Bean
     * @return MinioClient实例
     */
    @Bean("highConcurrencyMinioClient")
    public MinioClient highConcurrencyMinioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}