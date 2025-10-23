package com.nexus.file.controller;

import com.nexus.file.config.CDNConfig;
import com.nexus.file.util.CDNUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cdn")
public class CDNController {
    
    @Autowired
    private CDNConfig cdnConfig;
    
    @Autowired
    private CDNUtil cdnUtil;
    
    /**
     * 检查CDN配置状态
     * @return CDN配置信息和状态
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getCDNStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("enabled", cdnConfig.isCdnEnabled());
        status.put("domain", cdnConfig.getCdnDomain());
        status.put("protocol", cdnConfig.getCdnProtocol());
        status.put("valid", cdnUtil.isCDNConfigValid());
        status.put("baseUrl", cdnUtil.getCDNBaseUrl());
        
        return ResponseEntity.ok(status);
    }
}