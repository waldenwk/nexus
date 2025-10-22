package com.nexus.content.entity;

/**
 * 内容类型枚举
 * 定义用户可以发布的内容类型
 */
public enum PostType {
    /** 状态：简短的文字更新 */
    STATUS,      
    
    /** 日志：较长的博客文章 */
    BLOG,        
    
    /** 相册：图片集合 */
    ALBUM        
}