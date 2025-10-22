package com.nexus.content.controller;

import com.nexus.content.entity.Post;
import com.nexus.content.service.TimelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timeline")
public class TimelineController {

    @Autowired
    private TimelineService timelineService;

    /**
     * 获取用户时间轴
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 时间轴内容列表
     */
    @GetMapping("/{userId}")
    public List<Post> getUserTimeline(@PathVariable Long userId,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return timelineService.getUserTimeline(userId, page, size);
    }

    /**
     * 获取用户个人时间轴（仅自己的内容）
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 个人时间轴内容列表
     */
    @GetMapping("/{userId}/personal")
    public List<Post> getPersonalTimeline(@PathVariable Long userId,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return timelineService.getPersonalTimeline(userId, page, size);
    }
}