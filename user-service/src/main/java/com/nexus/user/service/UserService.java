package com.nexus.user.service;

import com.nexus.user.entity.User;
import com.nexus.user.repository.UserRepository;
import com.nexus.common.cache.MultilevelCacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务类，处理用户相关业务逻辑
 * 提供用户增删改查等基本操作
 */
@Service
public class UserService {
    
    /** 用户数据访问对象 */
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 获取所有用户列表
     * @return 用户列表
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * 根据ID获取特定用户
     * 使用多级缓存提升性能
     * @param id 用户ID
     * @return 用户对象，如果不存在则返回null
     */
    @MultilevelCacheable(cacheName = "users", key = "#id")
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    /**
     * 根据用户名获取用户
     * 使用多级缓存提升性能
     * @param username 用户名
     * @return 用户信息
     */
    @MultilevelCacheable(cacheName = "users", key = "#username")
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * 创建用户
     * @param user 用户对象
     * @return 创建后的用户对象
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    /**
     * 更新用户
     * @param user 用户对象
     * @return 更新后的用户对象
     */
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    /**
     * 保存用户（创建或更新）
     * @param user 用户对象
     * @return 保存后的用户对象
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    
    /**
     * 删除用户
     * @param id 用户ID
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    /**
     * 根据ID获取用户基本信息和资料
     * @param id 用户ID
     * @return 用户对象
     */
    public User getUserWithProfile(Long id) {
        // 这里可以添加获取用户详细资料的逻辑
        // 比如从user_profiles表中获取详细信息
        return getUserById(id);
    }
}