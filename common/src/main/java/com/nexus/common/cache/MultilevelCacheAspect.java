package com.nexus.common.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 多级缓存切面
 * 处理MultilevelCacheable注解
 */
@Aspect
@Component
public class MultilevelCacheAspect {

    @Autowired
    private MultilevelCacheManager multilevelCacheManager;

    private final ExpressionParser parser = new SpelExpressionParser();
    private final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    /**
     * 环绕通知处理多级缓存
     *
     * @param joinPoint 切点
     * @param multilevelCacheable 缓存注解
     * @return 方法执行结果
     * @throws Throwable 异常
     */
    @Around("@annotation(multilevelCacheable)")
    public Object around(ProceedingJoinPoint joinPoint, MultilevelCacheable multilevelCacheable) throws Throwable {
        // 获取缓存名称和键
        String cacheName = multilevelCacheable.cacheName();
        String keyExpression = multilevelCacheable.key();

        // 解析缓存键
        String key = parseKey(keyExpression, joinPoint);

        // 获取多级缓存
        MultilevelCacheManager.MultilevelCache cache = multilevelCacheManager.getMultilevelCache(cacheName);

        // 从缓存中获取数据
        Object result = cache.get(key, Object.class);

        // 缓存命中直接返回
        if (result != null) {
            return result;
        }

        // 缓存未命中，执行方法
        result = joinPoint.proceed();

        // 将结果放入缓存
        if (result != null) {
            cache.put(key, result);
        }

        return result;
    }

    /**
     * 解析缓存键
     *
     * @param keyExpression 键表达式
     * @param joinPoint 切点
     * @return 解析后的键
     */
    private String parseKey(String keyExpression, ProceedingJoinPoint joinPoint) {
        // 获取方法参数名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] paramNames = discoverer.getParameterNames(method);

        // 创建解析上下文
        EvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();

        // 将参数名和值放入上下文
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }

        // 解析表达式
        return parser.parseExpression(keyExpression).getValue(context, String.class);
    }
}