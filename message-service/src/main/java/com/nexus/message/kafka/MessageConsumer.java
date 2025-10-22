package com.nexus.message.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * 消息消费者
 */
@Service
public class MessageConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    
    // 私信主题
    private static final String PRIVATE_MESSAGE_TOPIC = "private-message";
    
    // 群组消息主题
    private static final String GROUP_MESSAGE_TOPIC = "group-message";
    
    // 通知主题
    private static final String NOTIFICATION_TOPIC = "notification";
    
    /**
     * 监听私信消息
     * @param message 消息内容
     */
    @KafkaListener(topics = PRIVATE_MESSAGE_TOPIC)
    public void listenPrivateMessage(String message) {
        logger.info("收到私信消息: {}", message);
        // 处理私信消息逻辑
        // 例如：保存到数据库，推送通知等
    }
    
    /**
     * 监听群组消息
     * @param message 消息内容
     */
    @KafkaListener(topics = GROUP_MESSAGE_TOPIC)
    public void listenGroupMessage(String message) {
        logger.info("收到群组消息: {}", message);
        // 处理群组消息逻辑
        // 例如：保存到数据库，推送通知等
    }
    
    /**
     * 监听通知消息
     * @param message 消息内容
     */
    @KafkaListener(topics = NOTIFICATION_TOPIC)
    public void listenNotification(String message) {
        logger.info("收到通知消息: {}", message);
        // 处理通知消息逻辑
        // 例如：发送推送通知，更新用户通知状态等
    }
}