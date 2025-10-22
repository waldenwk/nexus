package com.nexus.common.cookie;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie工具类
 * 用于处理分布式环境下的Cookie同步
 */
@Component
public class CookieUtil {
    
    /**
     * 设置Cookie
     * @param response HTTP响应
     * @param name Cookie名称
     * @param value Cookie值
     * @param maxAge 最大存活时间（秒）
     * @param domain 域名
     * @param path 路径
     * @param secure 是否仅HTTPS
     * @param httpOnly 是否仅HTTP
     */
    public void setCookie(HttpServletResponse response, String name, String value, 
                         int maxAge, String domain, String path, boolean secure, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        if (domain != null && !domain.isEmpty()) {
            cookie.setDomain(domain);
        }
        cookie.setPath(path);
        cookie.setSecure(secure);
        cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
    }
    
    /**
     * 获取Cookie值
     * @param request HTTP请求
     * @param name Cookie名称
     * @return Cookie值
     */
    public String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    
    /**
     * 删除Cookie
     * @param response HTTP响应
     * @param name Cookie名称
     * @param domain 域名
     * @param path 路径
     */
    public void deleteCookie(HttpServletResponse response, String name, String domain, String path) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        if (domain != null && !domain.isEmpty()) {
            cookie.setDomain(domain);
        }
        cookie.setPath(path);
        response.addCookie(cookie);
    }
}