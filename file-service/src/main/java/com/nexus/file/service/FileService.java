package com.nexus.file.service;

import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class FileService {
    
    @Autowired
    private MinioClient minioClient;
    
    @Value("${minio.bucket}")
    private String bucketName;
    
    /**
     * 保存文件到MinIO
     * @param file MultipartFile文件对象
     * @return 文件访问URL
     * @throws IOException IO异常
     */
    public String saveFile(MultipartFile file) throws IOException {
        try {
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + extension;
            
            // 检查bucket是否存在，不存在则创建
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            
            // 上传文件
            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(uniqueFilename)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
            }
            
            // 返回文件访问URL
            return getFileUrl(uniqueFilename);
        } catch (Exception e) {
            throw new IOException("文件上传失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取文件访问URL
     * @param fileName 文件名
     * @return 文件访问URL
     * @throws IOException IO异常
     */
    public String getFileUrl(String fileName) throws IOException {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(io.minio.http.Method.GET)
                            .bucket(bucketName)
                            .object(fileName)
                            .expiry(60 * 60 * 24) // 24小时有效期
                            .build());
        } catch (Exception e) {
            throw new IOException("获取文件URL失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 从MinIO获取文件数据
     * @param fileName 文件名
     * @return 文件数据
     * @throws IOException IO异常
     */
    public byte[] getFile(String fileName) throws IOException {
        try {
            GetObjectResponse response = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build());
            
            return response.readAllBytes();
        } catch (Exception e) {
            throw new IOException("文件下载失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 删除MinIO中的文件
     * @param fileName 文件名
     * @throws IOException IO异常
     */
    public void deleteFile(String fileName) throws IOException {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build());
        } catch (Exception e) {
            throw new IOException("文件删除失败: " + e.getMessage(), e);
        }
    }
}