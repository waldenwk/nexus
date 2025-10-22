package com.nexus.content.service;

import com.nexus.content.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostEventListener {

    @Autowired
    private FriendServiceClient friendServiceClient;

    @Autowired
    private FeedServiceClient feedServiceClient;

    /**
     * 当用户发布新内容时，将其推送到好友的feed中
     * @param post 新发布的内容
     */
    public void onPostCreated(Post post) {
        // 获取用户的好友列表
        // 注意：这应该是一个异步操作，为了简化示例，这里使用同步调用
        try {
            // 获取用户的好友列表
            // 这里实际调用user-service获取好友列表
            List<Long> friendIds = friendServiceClient.getUserFriendIds(post.getUserId());
            
            // 将内容推送到每个好友的feed中
            double score = post.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
            for (Long friendId : friendIds) {
                feedServiceClient.pushToFeed(friendId, post.getId(), score);
            }
            
            // 同时也推送到自己的feed中
            feedServiceClient.pushToFeed(post.getUserId(), post.getId(), score);
        } catch (Exception e) {
            // 实际应用中应该记录日志并进行错误处理
            e.printStackTrace();
        }
    }
}