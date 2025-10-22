package com.nexus.user.service;

import com.nexus.user.entity.RelationshipStatus;
import com.nexus.user.entity.UserRelationship;
import com.nexus.user.repository.UserRelationshipRepository;
import com.nexus.user.service.FeedServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 好友服务类，处理用户之间的好友关系相关业务逻辑
 * 包括发送好友请求、接受/拒绝好友请求、获取好友列表等操作
 */
@Service
public class FriendService {
    
    /** 用户关系数据访问对象 */
    @Autowired
    private UserRelationshipRepository userRelationshipRepository;
    
    /** Feed服务客户端，用于在好友关系变更时更新信息流 */
    @Autowired
    private FeedServiceClient feedServiceClient;
    
    /**
     * 发送好友请求
     * @param fromUserId 发送请求的用户ID
     * @param toUserId 接收请求的用户ID
     * @return 创建的用户关系对象
     */
    public UserRelationship sendFriendRequest(Long fromUserId, Long toUserId) {
        UserRelationship relationship = new UserRelationship(fromUserId, toUserId, RelationshipStatus.PENDING);
        return userRelationshipRepository.save(relationship);
    }
    
    /**
     * 接受好友请求
     * @param fromUserId 发送请求的用户ID
     * @param toUserId 接收请求的用户ID
     * @return 更新后的用户关系对象，如果不存在或状态不正确则返回null
     */
    public UserRelationship acceptFriendRequest(Long fromUserId, Long toUserId) {
        UserRelationship relationship = userRelationshipRepository.findByFromUserIdAndToUserId(fromUserId, toUserId);
        if (relationship != null && relationship.getStatus() == RelationshipStatus.PENDING) {
            relationship.setStatus(RelationshipStatus.ACCEPTED);
            // 创建双向关系，确保好友关系是对称的
            UserRelationship reverseRelationship = new UserRelationship(toUserId, fromUserId, RelationshipStatus.ACCEPTED);
            userRelationshipRepository.save(reverseRelationship);
            
            // 更新双方的信息流，确保好友的内容能够互相看到
            try {
                feedServiceClient.updateFeedOnFriendAdded(fromUserId, toUserId);
                feedServiceClient.updateFeedOnFriendAdded(toUserId, fromUserId);
            } catch (Exception e) {
                // 实际应用中应该记录日志
                e.printStackTrace();
            }
            
            return userRelationshipRepository.save(relationship);
        }
        return null;
    }
    
    /**
     * 拒绝好友请求
     * @param fromUserId 发送请求的用户ID
     * @param toUserId 接收请求的用户ID
     * @return 更新后的用户关系对象，如果不存在或状态不正确则返回null
     */
    public UserRelationship rejectFriendRequest(Long fromUserId, Long toUserId) {
        UserRelationship relationship = userRelationshipRepository.findByFromUserIdAndToUserId(fromUserId, toUserId);
        if (relationship != null && relationship.getStatus() == RelationshipStatus.PENDING) {
            relationship.setStatus(RelationshipStatus.BLOCKED);
            return userRelationshipRepository.save(relationship);
        }
        return null;
    }
    
    /**
     * 获取用户的好友列表
     * @param userId 用户ID
     * @return 已接受的好友关系列表
     */
    public List<UserRelationship> getUserFriends(Long userId) {
        return userRelationshipRepository.findByFromUserIdAndStatus(userId, RelationshipStatus.ACCEPTED);
    }
    
    /**
     * 获取用户的好友ID列表
     * @param userId 用户ID
     * @return 好友ID列表
     */
    public List<Long> getUserFriendIds(Long userId) {
        List<UserRelationship> relationships = getUserFriends(userId);
        return relationships.stream()
                .map(UserRelationship::getToUserId)
                .collect(Collectors.toList());
    }
}