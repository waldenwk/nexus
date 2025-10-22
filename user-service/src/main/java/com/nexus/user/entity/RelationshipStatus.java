package com.nexus.user.entity;

/**
 * 用户关系状态枚举
 * 定义用户之间关系的不同状态
 */
public enum RelationshipStatus {
    /** 待确认：好友请求已发送但未被接受 */
    PENDING,
    
    /** 已接受：好友关系已建立 */
    ACCEPTED,
    
    /** 已屏蔽：好友请求被拒绝或用户被屏蔽 */
    BLOCKED
}