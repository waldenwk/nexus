package com.nexus.content.controller;

import com.nexus.common.id.SnowflakeIdGenerator;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 相册事务控制器
 * 演示分布式事务的使用
 */
@RestController
@RequestMapping("/api/albums")
public class AlbumTransactionController {
    
    @Autowired
    private SnowflakeIdGenerator idGenerator;
    
    /**
     * 创建默认相册
     * 用于分布式事务演示
     * @param albumInfo 相册信息
     * @return 操作结果
     */
    @PostMapping("/create-default")
    @GlobalTransactional  // 开启全局事务
    public String createDefaultAlbum(@RequestBody String albumInfo) {
        try {
            // 解析相册信息
            // 这里应该实际创建相册
            
            // 模拟相册创建过程
            System.out.println("创建默认相册: " + albumInfo);
            
            // 模拟可能的异常情况，用于测试回滚
            // if (true) throw new RuntimeException("模拟异常，测试分布式事务回滚");
            
            return "默认相册创建成功";
        } catch (Exception e) {
            // 发生异常时，Seata会自动回滚整个分布式事务
            throw new RuntimeException("创建默认相册失败: " + e.getMessage());
        }
    }
}