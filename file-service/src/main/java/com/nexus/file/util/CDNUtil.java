package com.nexus.file.util;

import com.nexus.file.config.CDNConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * CDN工具类，提供CDN相关操作的辅助方法
 */
@Component
public class CDNUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(CDNUtil.class);
    
    @Autowired
    private CDNConfig cdnConfig;
    
    /**
     * 构建CDN URL
     * @param bucketName 存储桶名称
     * @param fileName 文件名
     * @return 完整的CDN URL
     */
    public String buildCDNUrl(String bucketName, String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            logger.warn("文件名不能为空");
            return null;
        }
        
        if (!cdnConfig.isCdnEnabled()) {
            logger.debug("CDN未启用");
            return null;
        }
        
        if (cdnConfig.getCdnDomain() == null || cdnConfig.getCdnDomain().isEmpty()) {
            logger.warn("CDN域名未配置");
            return null;
        }
        
        try {
            // 对文件名进行URL编码以处理特殊字符
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20"); // 将+替换为空格
            
            String cdnUrl = cdnConfig.getCdnProtocol() + "://" + 
                   cdnConfig.getCdnDomain() + "/" + 
                   bucketName + "/" + 
                   encodedFileName;
                   
            logger.debug("构建CDN URL: {}", cdnUrl);
            return cdnUrl;
        } catch (Exception e) {
            logger.error("构建CDN URL时发生错误", e);
            return null;
        }
    }
    
    /**
     * 验证CDN配置是否有效
     * @return CDN配置是否有效
     */
    public boolean isCDNConfigValid() {
        boolean valid = cdnConfig.isCdnEnabled() && 
               cdnConfig.getCdnDomain() != null && 
               !cdnConfig.getCdnDomain().isEmpty();
               
        logger.debug("CDN配置验证结果: {}", valid);
        return valid;
    }
    
    /**
     * 获取CDN基础URL
     * @return CDN基础URL
     */
    public String getCDNBaseUrl() {
        if (!isCDNConfigValid()) {
            logger.debug("CDN配置无效，无法获取基础URL");
            return null;
        }
        
        String baseUrl = cdnConfig.getCdnProtocol() + "://" + cdnConfig.getCdnDomain();
        logger.debug("获取CDN基础URL: {}", baseUrl);
        return baseUrl;
    }
}