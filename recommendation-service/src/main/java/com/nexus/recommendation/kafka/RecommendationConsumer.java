package com.nexus.recommendation.kafka;

import com.nexus.recommendation.service.RecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * 推荐服务Kafka消费者
 * 监听用户行为事件，用于更新用户偏好和优化推荐算法
 */
@Service
public class RecommendationConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(RecommendationConsumer.class);
    
    // 用户行为主题
    private static final String USER_ACTION_TOPIC = "user-action";
    
    // 内容互动主题
    private static final String CONTENT_INTERACTION_TOPIC = "content-interaction";
    
    @Autowired
    private RecommendationService recommendationService;
    
    /**
     * 监听用户行为事件
     * @param message 用户行为消息
     */
    @KafkaListener(topics = USER_ACTION_TOPIC)
    public void listenUserAction(String message) {
        logger.info("收到用户行为事件: {}", message);
        // 解析消息并更新用户偏好
        // 例如：用户浏览了某个标签的内容，增加该标签的偏好权重
        // 这里需要根据实际的消息格式进行解析和处理
    }
    
    /**
     * 监听内容互动事件
     * @param message 内容互动消息
     */
    @KafkaListener(topics = CONTENT_INTERACTION_TOPIC)
    public void listenContentInteraction(String message) {
        logger.info("收到内容互动事件: {}", message);
        // 解析消息并更新用户偏好
        // 例如：用户点赞了某类内容，增加该类内容的偏好权重
        // 这里需要根据实际的消息格式进行解析和处理
    }
}