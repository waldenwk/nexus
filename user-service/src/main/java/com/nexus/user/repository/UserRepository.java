package com.nexus.user.repository;

import com.nexus.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户数据访问接口
 * 继承JPA Repository，提供基本的CRUD操作
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}