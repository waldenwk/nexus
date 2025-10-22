package com.nexus.group.service;

import com.nexus.group.entity.Group;
import com.nexus.group.entity.GroupMember;
import com.nexus.group.repository.GroupMemberRepository;
import com.nexus.group.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    
    public Group createGroup(Group group) {
        group.setCreatedAt(java.time.LocalDateTime.now());
        group.setUpdatedAt(java.time.LocalDateTime.now());
        return groupRepository.save(group);
    }
    
    public Group getGroupById(Long id) {
        return groupRepository.findById(id).orElse(null);
    }
    
    public GroupMember addMember(Long groupId, GroupMember member) {
        member.setGroupId(groupId);
        member.setJoinedAt(java.time.LocalDateTime.now());
        return groupMemberRepository.save(member);
    }
    
    public List<GroupMember> getGroupMembers(Long groupId) {
        return groupMemberRepository.findByGroupId(groupId);
    }
}