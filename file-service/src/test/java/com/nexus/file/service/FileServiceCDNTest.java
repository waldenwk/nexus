package com.nexus.file.service;

import com.nexus.file.config.CDNConfig;
import com.nexus.file.util.CDNUtil;
import io.minio.MinioClient;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.errors.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FileServiceCDNTest {

    @Mock
    private MinioClient minioClient;

    @Mock
    private MinioClient highConcurrencyMinioClient;

    @Mock
    private CDNConfig cdnConfig;

    @Mock
    private CDNUtil cdnUtil;

    @InjectMocks
    private FileService fileService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // 使用反射设置 bucketName 值，因为 @Value 注解在测试环境中不会自动注入
        try {
            java.lang.reflect.Field bucketNameField = FileService.class.getDeclaredField("bucketName");
            bucketNameField.setAccessible(true);
            bucketNameField.set(fileService, "test-bucket");
        } catch (Exception e) {
            fail("无法设置 bucketName 字段");
        }
    }

    @Test
    void testGetFileUrl_WithCDNEnabled() throws Exception {
        // 准备测试数据
        String fileName = "test-file.jpg";
        String expectedCDNUrl = "https://cdn.example.com/test-bucket/test-file.jpg";

        // 设置 mock 行为
        when(cdnUtil.buildCDNUrl("test-bucket", fileName)).thenReturn(expectedCDNUrl);

        // 执行测试
        String url = fileService.getFileUrl(fileName);

        // 验证结果
        assertEquals(expectedCDNUrl, url);
    }

    @Test
    void testGetFileUrl_WithCDNDisabled() throws Exception {
        // 准备测试数据
        String fileName = "test-file.jpg";
        String expectedMinioUrl = "https://minio.example.com/test-bucket/test-file.jpg";

        // 设置 mock 行为
        when(cdnUtil.buildCDNUrl("test-bucket", fileName)).thenReturn(null);
        when(minioClient.getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class)))
                .thenReturn(expectedMinioUrl);

        // 执行测试
        String url = fileService.getFileUrl(fileName);

        // 验证结果
        assertEquals(expectedMinioUrl, url);
    }

    @Test
    void testGetFileUrl_MinioException() throws Exception {
        // 准备测试数据
        String fileName = "test-file.jpg";

        // 设置 mock 行为
        when(cdnUtil.buildCDNUrl("test-bucket", fileName)).thenReturn(null);
        when(minioClient.getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class)))
                .thenThrow(new InsufficientDataException("Insufficient data"));

        // 执行测试并验证异常
        assertThrows(IOException.class, () -> {
            fileService.getFileUrl(fileName);
        });
    }
}