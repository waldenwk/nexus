package com.nexus.message.controller;

import com.nexus.message.entity.PrivateMessage;
import com.nexus.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    @PostMapping
    public PrivateMessage sendMessage(@RequestBody PrivateMessage message) {
        return messageService.sendMessage(message);
    }
    
    @GetMapping("/conversation")
    public List<PrivateMessage> getConversation(@RequestParam Long user1Id, @RequestParam Long user2Id) {
        return messageService.getConversation(user1Id, user2Id);
    }
    
    @PutMapping("/{id}/read")
    public ResponseEntity<PrivateMessage> markAsRead(@PathVariable Long id) {
        PrivateMessage message = messageService.markAsRead(id);
        if (message != null) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}