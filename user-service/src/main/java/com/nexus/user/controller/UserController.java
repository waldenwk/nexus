package com.nexus.user.controller;

import com.nexus.user.entity.User;
import com.nexus.user.service.UserService;
import com.nexus.common.id.SnowflakeIdGenerator;
import com.nexus.common.security.JwtUtil;
import com.nexus.common.session.DistributedSessionManager;
import com.nexus.common.cookie.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户控制器
 * 处理用户相关的HTTP请求
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SnowflakeIdGenerator idGenerator;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private DistributedSessionManager sessionManager;
    
    @Autowired
    private CookieUtil cookieUtil;
    
    /**
     * 获取所有用户
     * @return 用户列表
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    
    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
    
    /**
     * 创建用户
     * @param user 用户信息
     * @return 创建的用户
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        // 使用分布式ID生成器生成唯一ID
        Long userId = idGenerator.nextId();
        user.setId(userId);
        return userService.createUser(user);
    }
    
    /**
     * 用户登录
     * @param user 登录信息
     * @param response HTTP响应
     * @return 登录结果
     */
    @PostMapping("/login")
    public String login(@RequestBody User user, HttpServletResponse response) {
        // 这里应该验证用户凭据
        User authenticatedUser = userService.getUserByUsername(user.getUsername());
        
        if (authenticatedUser != null) {
            // 生成JWT Token
            String token = jwtUtil.generateToken(authenticatedUser);
            
            // 创建分布式Session
            Long sessionIdLong = idGenerator.nextId();
            String sessionId = sessionIdLong.toString();
            sessionManager.createSession(sessionId, authenticatedUser.getId(), authenticatedUser);
            
            // 设置Cookie
            cookieUtil.setCookie(response, "SESSIONID", sessionId, 1800, null, "/", false, true);
            cookieUtil.setCookie(response, "TOKEN", token, 1800, null, "/", false, true);
            
            return "登录成功";
        } else {
            return "用户名或密码错误";
        }
    }
    
    /**
     * 用户登出
     * @param request HTTP请求
     * @param response HTTP响应
     * @return 登出结果
     */
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // 从Cookie中获取Session ID
        String sessionId = cookieUtil.getCookieValue(request, "SESSIONID");
        
        if (sessionId != null) {
            // 删除分布式Session
            sessionManager.removeSession("session:" + sessionId);
            
            // 删除Cookie
            cookieUtil.deleteCookie(response, "SESSIONID", null, "/");
            cookieUtil.deleteCookie(response, "TOKEN", null, "/");
        }
        
        return "登出成功";
    }
    
    /**
     * 更新用户
     * @param id 用户ID
     * @param user 用户信息
     * @return 更新后的用户
     */
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return userService.updateUser(user);
    }
    
    /**
     * 删除用户
     * @param id 用户ID
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}