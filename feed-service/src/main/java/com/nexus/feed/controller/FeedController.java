package com.nexus.feed.controller;

import com.nexus.feed.service.FeedService;
import com.nexus.feed.service.FeedUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/feed")
public class FeedController {
    
    @Autowired
    private FeedService feedService;
    
    @Autowired
    private FeedUpdateService feedUpdateService;
    
    @PostMapping("/push")
    public void pushToFeed(@RequestParam Long userId, 
                          @RequestParam Long contentId, 
                          @RequestParam(defaultValue = "0") double score) {
        feedService.pushToFeed(userId, contentId, score);
    }
    
    @GetMapping("/{userId}")
    public List<Long> getUserFeed(@PathVariable Long userId,
                                  @RequestParam(defaultValue = "0") int start,
                                  @RequestParam(defaultValue = "10") int end) {
        Set<String> feed = feedService.getUserFeed(userId, start, end);
        return feed.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
    
    @DeleteMapping("/remove")
    public void removeFromFeed(@RequestParam Long userId, @RequestParam Long contentId) {
        feedService.removeFromFeed(userId, contentId);
    }
    
    @PostMapping("/update/friend-added")
    public void updateFeedOnFriendAdded(@RequestParam Long userId, @RequestParam Long friendId) {
        feedUpdateService.updateFeedOnFriendAdded(userId, friendId);
    }
    
    @PostMapping("/update/friend-removed")
    public void updateFeedOnFriendRemoved(@RequestParam Long userId, @RequestParam Long friendId) {
        feedUpdateService.updateFeedOnFriendRemoved(userId, friendId);
    }
}