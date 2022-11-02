package com.example.learn.domain.user;

import com.example.learn.infrastructure.database.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IUserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);
}
