package com.nexus.file.integration;

import com.nexus.file.FileServiceApplication;
import com.nexus.file.config.CDNConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FileServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CDNIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CDNConfig cdnConfig;

    @Test
    public void testCDNStatusEndpoint() {
        String url = "http://localhost:" + port + "/api/cdn/status";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).containsKeys("enabled", "domain", "protocol", "valid", "baseUrl");
    }

    @Test
    public void testCDNConfiguration() {
        // 测试默认配置
        assertThat(cdnConfig.isCdnEnabled()).isFalse();
        assertThat(cdnConfig.getCdnDomain()).isEmpty();
        assertThat(cdnConfig.getCdnProtocol()).isEqualTo("https");
    }
}