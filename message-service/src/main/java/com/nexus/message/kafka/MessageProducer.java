package com.nexus.message.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * 消息生产者
 */
@Service
public class MessageProducer {
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    // 私信主题
    private static final String PRIVATE_MESSAGE_TOPIC = "private-message";
    
    // 群组消息主题
    private static final String GROUP_MESSAGE_TOPIC = "group-message";
    
    // 通知主题
    private static final String NOTIFICATION_TOPIC = "notification";
    
    /**
     * 发送私信消息
     * @param message 消息内容
     */
    public void sendPrivateMessage(String message) {
        kafkaTemplate.send(PRIVATE_MESSAGE_TOPIC, message);
    }
    
    /**
     * 发送群组消息
     * @param message 消息内容
     */
    public void sendGroupMessage(String message) {
        kafkaTemplate.send(GROUP_MESSAGE_TOPIC, message);
    }
    
    /**
     * 发送通知
     * @param message 通知内容
     */
    public void sendNotification(String message) {
        kafkaTemplate.send(NOTIFICATION_TOPIC, message);
    }
}