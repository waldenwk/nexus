package com.nexus.common.transaction;

import io.seata.spring.annotation.GlobalTransactionScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Seata分布式事务配置
 */
@Configuration
public class TransactionConfig {
    
    /**
     * 初始化全局事务扫描器
     * @return 全局事务扫描器
     */
    @Bean
    public GlobalTransactionScanner globalTransactionScanner() {
        // 参数1: 应用名称
        // 参数2: 事务组名称
        return new GlobalTransactionScanner("nexus-common", "nexus-tx-group");
    }
}