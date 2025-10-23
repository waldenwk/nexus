package com.nexus.file.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * CDN配置类
 */
@Configuration
public class CDNConfig {
    
    @Value("${cdn.enabled:false}")
    private boolean cdnEnabled;
    
    @Value("${cdn.domain:}")
    private String cdnDomain;
    
    @Value("${cdn.protocol:https}")
    private String cdnProtocol;
    
    /**
     * 检查CDN是否启用
     * @return 是否启用CDN
     */
    public boolean isCdnEnabled() {
        return cdnEnabled;
    }
    
    /**
     * 获取CDN域名
     * @return CDN域名
     */
    public String getCdnDomain() {
        return cdnDomain;
    }
    
    /**
     * 获取CDN协议
     * @return CDN协议 (http 或 https)
     */
    public String getCdnProtocol() {
        return cdnProtocol;
    }
    
    /**
     * 获取完整的CDN基础URL
     * @return CDN基础URL
     */
    public String getCdnBaseUrl() {
        if (!cdnEnabled || cdnDomain == null || cdnDomain.isEmpty()) {
            return "";
        }
        return cdnProtocol + "://" + cdnDomain;
    }
}