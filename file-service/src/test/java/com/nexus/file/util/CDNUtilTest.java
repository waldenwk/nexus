package com.nexus.file.util;

import com.nexus.file.config.CDNConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CDNUtilTest {
    
    @Mock
    private CDNConfig cdnConfig;
    
    @InjectMocks
    private CDNUtil cdnUtil;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testBuildCDNUrl_WithValidConfig() {
        // 准备测试数据
        when(cdnConfig.isCdnEnabled()).thenReturn(true);
        when(cdnConfig.getCdnDomain()).thenReturn("cdn.example.com");
        when(cdnConfig.getCdnProtocol()).thenReturn("https");
        
        String bucketName = "test-bucket";
        String fileName = "test-file.jpg";
        
        // 执行测试
        String cdnUrl = cdnUtil.buildCDNUrl(bucketName, fileName);
        
        // 验证结果
        assertNotNull(cdnUrl);
        assertEquals("https://cdn.example.com/test-bucket/test-file.jpg", cdnUrl);
    }
    
    @Test
    public void testBuildCDNUrl_WithSpecialCharactersInFileName() {
        // 准备测试数据
        when(cdnConfig.isCdnEnabled()).thenReturn(true);
        when(cdnConfig.getCdnDomain()).thenReturn("cdn.example.com");
        when(cdnConfig.getCdnProtocol()).thenReturn("https");
        
        String bucketName = "test-bucket";
        String fileName = "test file with spaces.jpg";
        
        // 执行测试
        String cdnUrl = cdnUtil.buildCDNUrl(bucketName, fileName);
        
        // 验证结果
        assertNotNull(cdnUrl);
        assertEquals("https://cdn.example.com/test-bucket/test%20file%20with%20spaces.jpg", cdnUrl);
    }
    
    @Test
    public void testBuildCDNUrl_WithCDNDisabled() {
        // 准备测试数据
        when(cdnConfig.isCdnEnabled()).thenReturn(false);
        
        String bucketName = "test-bucket";
        String fileName = "test-file.jpg";
        
        // 执行测试
        String cdnUrl = cdnUtil.buildCDNUrl(bucketName, fileName);
        
        // 验证结果
        assertNull(cdnUrl);
    }
    
    @Test
    public void testBuildCDNUrl_WithEmptyDomain() {
        // 准备测试数据
        when(cdnConfig.isCdnEnabled()).thenReturn(true);
        when(cdnConfig.getCdnDomain()).thenReturn("");
        
        String bucketName = "test-bucket";
        String fileName = "test-file.jpg";
        
        // 执行测试
        String cdnUrl = cdnUtil.buildCDNUrl(bucketName, fileName);
        
        // 验证结果
        assertNull(cdnUrl);
    }
    
    @Test
    public void testIsCDNConfigValid_ValidConfig() {
        // 准备测试数据
        when(cdnConfig.isCdnEnabled()).thenReturn(true);
        when(cdnConfig.getCdnDomain()).thenReturn("cdn.example.com");
        
        // 执行测试
        boolean isValid = cdnUtil.isCDNConfigValid();
        
        // 验证结果
        assertTrue(isValid);
    }
    
    @Test
    public void testIsCDNConfigValid_InvalidConfig() {
        // 准备测试数据
        when(cdnConfig.isCdnEnabled()).thenReturn(false);
        
        // 执行测试
        boolean isValid = cdnUtil.isCDNConfigValid();
        
        // 验证结果
        assertFalse(isValid);
    }
    
    @Test
    public void testGetCDNBaseUrl_ValidConfig() {
        // 准备测试数据
        when(cdnConfig.isCdnEnabled()).thenReturn(true);
        when(cdnConfig.getCdnDomain()).thenReturn("cdn.example.com");
        when(cdnConfig.getCdnProtocol()).thenReturn("https");
        
        // 执行测试
        String baseUrl = cdnUtil.getCDNBaseUrl();
        
        // 验证结果
        assertNotNull(baseUrl);
        assertEquals("https://cdn.example.com", baseUrl);
    }
    
    @Test
    public void testGetCDNBaseUrl_InvalidConfig() {
        // 准备测试数据
        when(cdnConfig.isCdnEnabled()).thenReturn(false);
        
        // 执行测试
        String baseUrl = cdnUtil.getCDNBaseUrl();
        
        // 验证结果
        assertNull(baseUrl);
    }
}