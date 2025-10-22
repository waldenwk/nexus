package com.nexus.user.controller;

import com.nexus.user.entity.User;
import com.nexus.user.service.UserService;
import com.nexus.common.id.SnowflakeIdGenerator;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * 用户事务控制器
 * 演示分布式事务的使用
 */
@RestController
@RequestMapping("/api/user-transactions")
public class UserTransactionController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SnowflakeIdGenerator idGenerator;
    
    @Autowired
    private RestTemplate restTemplate;
    
    /**
     * 创建用户并初始化相册
     * 演示分布式事务：同时在用户服务创建用户，在内容服务创建默认相册
     * @param user 用户信息
     * @return 操作结果
     */
    @PostMapping("/user-with-album")
    @GlobalTransactional  // 开启全局事务
    public String createUserWithAlbum(@RequestBody User user) {
        try {
            // 1. 在用户服务创建用户
            Long userId = idGenerator.nextId();
            user.setId(userId);
            userService.createUser(user);
            
            // 2. 调用内容服务创建默认相册
            String contentServiceUrl = "http://content-service/api/albums/create-default";
            
            // 构造相册信息
            String albumInfo = "{ \"userId\": " + userId + ", \"name\": \"默认相册\", \"description\": \"用户默认相册\" }";
            
            // 调用内容服务
            String result = restTemplate.postForObject(contentServiceUrl, albumInfo, String.class);
            
            // 模拟可能的异常情况，用于测试回滚
            // if (true) throw new RuntimeException("模拟异常，测试分布式事务回滚");
            
            return "用户和默认相册创建成功";
        } catch (Exception e) {
            // 发生异常时，Seata会自动回滚整个分布式事务
            throw new RuntimeException("创建用户和相册失败: " + e.getMessage());
        }
    }
}