package com.nexus.user.entity;

/**
 * 联系信息可见性枚举
 * 定义用户联系信息的可见性级别
 */
public enum ContactVisibility {
    /** 公开：所有用户都可以查看 */
    PUBLIC,
    
    /** 仅好友：只有好友可以查看 */
    FRIENDS_ONLY,
    
    /** 仅自己：只有自己可以查看 */
    PRIVATE
}