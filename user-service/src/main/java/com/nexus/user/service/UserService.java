package com.nexus.user.service;

import com.nexus.user.entity.User;
import com.nexus.user.repository.UserRepository;
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
     * @param id 用户ID
     * @return 用户对象，如果不存在则返回null
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
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
}