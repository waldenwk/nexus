package com.nexus.file.service;

import com.nexus.file.config.CDNConfig;
import com.nexus.file.util.CDNUtil;
import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class FileService {
    
    @Autowired
    private MinioClient minioClient;
    
    // 用于高并发场景的MinioClient
    @Autowired
    @Qualifier("highConcurrencyMinioClient")
    private MinioClient highConcurrencyMinioClient;
    
    @Value("${minio.bucket}")
    private String bucketName;
    
    // CDN配置
    @Autowired
    private CDNConfig cdnConfig;
    
    @Autowired
    private CDNUtil cdnUtil;
    
    // 创建线程池用于异步处理文件上传
    private final ExecutorService executorService = Executors.newFixedThreadPool(20);
    
    /**
     * 异步保存文件到MinIO
     * @param file MultipartFile文件对象
     * @return CompletableFuture包含文件访问URL
     */
    public CompletableFuture<String> saveFileAsync(MultipartFile file) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return saveFile(file);
            } catch (IOException e) {
                throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
            }
        }, executorService);
    }
    
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
     * 高并发场景下保存文件到MinIO
     * @param file MultipartFile文件对象
     * @return 文件访问URL
     * @throws IOException IO异常
     */
    public String saveFileHighConcurrency(MultipartFile file) throws IOException {
        try {
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + extension;
            
            // 检查bucket是否存在，不存在则创建
            boolean bucketExists = highConcurrencyMinioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExists) {
                highConcurrencyMinioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            
            // 上传文件
            try (InputStream inputStream = file.getInputStream()) {
                highConcurrencyMinioClient.putObject(
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
     * 批量保存文件到MinIO
     * @param files MultipartFile文件对象数组
     * @return 文件访问URL数组
     * @throws IOException IO异常
     */
    public String[] saveFiles(MultipartFile[] files) throws IOException {
        String[] urls = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            urls[i] = saveFile(files[i]);
        }
        return urls;
    }
    
    /**
     * 高并发场景下批量保存文件到MinIO
     * @param files MultipartFile文件对象数组
     * @return 文件访问URL数组
     * @throws IOException IO异常
     */
    public String[] saveFilesHighConcurrency(MultipartFile[] files) throws IOException {
        String[] urls = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            urls[i] = saveFileHighConcurrency(files[i]);
        }
        return urls;
    }
    
    /**
     * 异步批量保存文件到MinIO
     * @param files MultipartFile文件对象数组
     * @return CompletableFuture包含文件访问URL数组
     */
    public CompletableFuture<String[]> saveFilesAsync(MultipartFile[] files) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return saveFiles(files);
            } catch (IOException e) {
                throw new RuntimeException("批量文件上传失败: " + e.getMessage(), e);
            }
        }, executorService);
    }
    
    /**
     * 高并发场景下异步批量保存文件到MinIO
     * @param files MultipartFile文件对象数组
     * @return CompletableFuture包含文件访问URL数组
     */
    public CompletableFuture<String[]> saveFilesHighConcurrencyAsync(MultipartFile[] files) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return saveFilesHighConcurrency(files);
            } catch (IOException e) {
                throw new RuntimeException("批量文件上传失败: " + e.getMessage(), e);
            }
        }, executorService);
    }
    
    /**
     * 获取文件访问URL
     * 如果启用了CDN且配置有效，将返回CDN URL，否则返回MinIO预签名URL
     * @param fileName 文件名
     * @return 文件访问URL
     * @throws IOException IO异常
     */
    public String getFileUrl(String fileName) throws IOException {
        try {
            // 如果启用了CDN，则返回CDN URL
            String cdnUrl = cdnUtil.buildCDNUrl(bucketName, fileName);
            if (cdnUrl != null) {
                return cdnUrl;
            }
            
            // 否则返回MinIO预签名URL
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
    
    /**
     * 关闭线程池
     */
    public void shutdown() {
        executorService.shutdown();
    }
}