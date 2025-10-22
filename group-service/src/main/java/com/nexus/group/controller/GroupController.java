package com.nexus.group.controller;

import com.nexus.group.entity.Group;
import com.nexus.group.entity.GroupMember;
import com.nexus.group.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    
    @Autowired
    private GroupService groupService;
    
    @PostMapping
    public Group createGroup(@RequestBody Group group) {
        return groupService.createGroup(group);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroup(@PathVariable Long id) {
        Group group = groupService.getGroupById(id);
        if (group != null) {
            return ResponseEntity.ok(group);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{groupId}/members")
    public GroupMember addMember(@PathVariable Long groupId, @RequestBody GroupMember member) {
        return groupService.addMember(groupId, member);
    }
    
    @GetMapping("/{groupId}/members")
    public List<GroupMember> getGroupMembers(@PathVariable Long groupId) {
        return groupService.getGroupMembers(groupId);
    }
}