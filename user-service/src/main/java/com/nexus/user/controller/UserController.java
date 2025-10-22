package com.nexus.user.controller;

import com.nexus.user.entity.User;
import com.nexus.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器，处理用户相关的REST API请求
 * 提供用户增删改查等基本操作接口
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    /** 用户服务实例 */
    @Autowired
    private UserService userService;
    
    /**
     * 获取所有用户列表
     * @return 用户列表
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    
    /**
     * 根据ID获取特定用户
     * @param id 用户ID
     * @return 用户信息或404错误
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 创建新用户
     * @param user 用户信息
     * @return 创建的用户对象
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
    
    /**
     * 更新用户信息
     * @param id 用户ID
     * @param userDetails 更新的用户详细信息
     * @return 更新后的用户对象或404错误
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userService.getUserById(id);
        if (user != null) {
            user.setUsername(userDetails.getUsername());
            user.setRealName(userDetails.getRealName());
            user.setSchool(userDetails.getSchool());
            user.setEnrollmentYear(userDetails.getEnrollmentYear());
            user.setUpdatedAt(java.time.LocalDateTime.now());
            
            User updatedUser = userService.saveUser(user);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 200成功或404错误
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}