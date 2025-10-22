package com.nexus.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 * 配置Web相关设置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private DistributedSessionFilter distributedSessionFilter;
    
    /**
     * 注册分布式Session过滤器
     * @return 过滤器注册Bean
     */
    @Bean
    public FilterRegistrationBean<DistributedSessionFilter> loggingFilter(){
        FilterRegistrationBean<DistributedSessionFilter> registrationBean = new FilterRegistrationBean<>();
        
        registrationBean.setFilter(distributedSessionFilter);
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);
        
        return registrationBean;
    }
    
    // 可以在这里添加拦截器、资源映射等配置
    
}