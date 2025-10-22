package com.nexus.common.web;

import com.nexus.common.cookie.CookieUtil;
import com.nexus.common.session.DistributedSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 分布式Session过滤器
 * 用于在分布式环境中同步Session和Cookie
 */
@Component
public class DistributedSessionFilter implements Filter {
    
    @Autowired
    private DistributedSessionManager sessionManager;
    
    @Autowired
    private CookieUtil cookieUtil;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // 从Cookie中获取Session ID
        String sessionId = cookieUtil.getCookieValue(httpRequest, "SESSIONID");
        
        if (sessionId != null && !sessionId.isEmpty()) {
            // 从分布式Session中获取用户信息
            Object userInfo = sessionManager.getSession(sessionId);
            if (userInfo != null) {
                // 刷新Session过期时间
                sessionManager.refreshSession(sessionId);
                // 将用户信息存储到请求属性中，供后续使用
                httpRequest.setAttribute("currentUser", userInfo);
            } else {
                // Session已过期，删除Cookie
                cookieUtil.deleteCookie(httpResponse, "SESSIONID", null, "/");
            }
        }
        
        chain.doFilter(request, response);
    }
}