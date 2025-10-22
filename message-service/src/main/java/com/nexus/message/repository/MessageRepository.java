package com.nexus.message.repository;

import com.nexus.message.entity.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<PrivateMessage, Long> {
    
    @Query("SELECT m FROM PrivateMessage m WHERE (m.fromUserId = ?1 AND m.toUserId = ?2) OR (m.fromUserId = ?2 AND m.toUserId = ?1) ORDER BY m.createdAt ASC")
    List<PrivateMessage> findConversation(Long user1Id, Long user2Id);
}