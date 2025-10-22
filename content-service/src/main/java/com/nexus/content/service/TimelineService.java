package com.nexus.content.service;

import com.nexus.content.entity.Post;
import com.nexus.content.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 时间轴服务类，负责处理用户时间轴相关内容
 * 包括获取好友信息流和个人时间轴等操作
 */
@Service
public class TimelineService {

    /** 内容数据访问对象 */
    @Autowired
    private PostRepository postRepository;

    /** 好友服务客户端，用于获取用户好友信息 */
    @Autowired
    private FriendServiceClient friendServiceClient;

    /** Feed服务客户端，用于获取用户信息流 */
    @Autowired
    private FeedServiceClient feedServiceClient;

    /**
     * 获取用户时间轴（包含好友的内容）
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 时间轴内容列表
     */
    public List<Post> getUserTimeline(Long userId, int page, int size) {
        // 从feed服务获取用户feed
        List<Long> postIds = feedServiceClient.getUserFeed(userId, page * size, (page + 1) * size - 1);
        
        // 根据ID获取具体的内容
        return postRepository.findByIdIn(postIds);
    }

    /**
     * 获取用户个人时间轴（仅自己的内容）
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 个人时间轴内容列表
     */
    public List<Post> getPersonalTimeline(Long userId, int page, int size) {
        // 直接从数据库获取用户自己的内容
        return postRepository.findByUserIdOrderByCreatedAtDesc(userId, 
            org.springframework.data.domain.PageRequest.of(page, size));
    }
}