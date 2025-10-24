package com.nexus.user.repository;

import com.nexus.user.entity.RelationshipStatus;
import com.nexus.user.entity.UserRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户关系数据访问接口
 * 继承JPA Repository，提供基本的CRUD操作和自定义查询方法
 */
@Repository
public interface UserRelationshipRepository extends JpaRepository<UserRelationship, Long> {
    /**
     * 根据发送用户ID和接收用户ID查找关系
     * @param fromUserId 发送用户ID
     * @param toUserId 接收用户ID
     * @return 用户关系对象
     */
    UserRelationship findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);
    
    /**
     * 根据发送用户ID和关系状态查找关系列表
     * @param fromUserId 发送用户ID
     * @param status 关系状态
     * @return 用户关系列表
     */
    List<UserRelationship> findByFromUserIdAndStatus(Long fromUserId, RelationshipStatus status);
    
    /**
     * 根据发送用户ID列表和关系状态查找关系列表
     * @param fromUserIds 发送用户ID列表
     * @param status 关系状态
     * @return 用户关系列表
     */
    List<UserRelationship> findByFromUserIdInAndStatus(List<Long> fromUserIds, RelationshipStatus status);
    
    /**
     * 根据接收用户ID和关系状态查找关系列表
     * @param toUserId 接收用户ID
     * @param status 关系状态
     * @return 用户关系列表
     */
    List<UserRelationship> findByToUserIdAndStatus(Long toUserId, RelationshipStatus status);
}