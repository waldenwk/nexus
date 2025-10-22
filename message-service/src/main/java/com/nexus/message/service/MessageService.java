package com.nexus.message.service;

import com.nexus.message.entity.PrivateMessage;
import com.nexus.message.kafka.MessageProducer;
import com.nexus.message.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private MessageProducer messageProducer;
    
    /**
     * 异步发送消息通知
     * @param message 消息内容
     */
    @Async
    public CompletableFuture<Void> sendNotificationAsync(String message) {
        messageProducer.sendPrivateMessage(message);
        return CompletableFuture.completedFuture(null);
    }
    
    /**
     * 发送消息
     * @param message 消息对象
     * @return 发送后的消息对象
     */
    public PrivateMessage sendMessage(PrivateMessage message) {
        message.setTimestamp(LocalDateTime.now());
        message.setRead(false);
        PrivateMessage savedMessage = messageRepository.save(message);
        
        // 异步发送消息通知，避免阻塞主线程
        sendNotificationAsync("新私信: from " + message.getSenderId() + " to " + message.getReceiverId());
        
        return savedMessage;
    }
    
    /**
     * 批量发送消息
     * @param messages 消息列表
     * @return 发送后的消息列表
     */
    public List<PrivateMessage> sendMessages(List<PrivateMessage> messages) {
        for (PrivateMessage message : messages) {
            message.setTimestamp(LocalDateTime.now());
            message.setRead(false);
        }
        List<PrivateMessage> savedMessages = messageRepository.saveAll(messages);
        
        // 异步发送批量消息通知
        sendNotificationAsync("批量私信发送，共" + messages.size() + "条消息");
        
        return savedMessages;
    }
    
    /**
     * 获取会话消息
     * @param user1Id 用户1 ID
     * @param user2Id 用户2 ID
     * @return 消息列表
     */
    public List<PrivateMessage> getConversation(Long user1Id, Long user2Id) {
        return messageRepository.findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByTimestamp(
                user1Id, user2Id, user2Id, user1Id);
    }
    
    /**
     * 标记消息为已读
     * @param id 消息ID
     * @return 更新后的消息对象
     */
    public PrivateMessage markAsRead(Long id) {
        return messageRepository.findById(id).map(message -> {
            message.setRead(true);
            return messageRepository.save(message);
        }).orElse(null);
    }
}