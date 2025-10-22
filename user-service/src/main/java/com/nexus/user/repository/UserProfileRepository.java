package com.nexus.user.repository;

import com.nexus.user.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户资料数据访问接口
 * 继承JPA Repository，提供基本的CRUD操作和自定义查询方法
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    /**
     * 根据用户ID查找用户资料
     * @param userId 用户ID
     * @return 用户资料对象
     */
    Optional<UserProfile> findByUserId(Long userId);
    
    /**
     * 根据用户ID删除用户资料
     * @param userId 用户ID
     * @return 删除的记录数
     */
    int deleteByUserId(Long userId);
}