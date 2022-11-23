package com.example.learn.application;

import com.example.learn.domain.api.user.IUserRepository;
import com.example.learn.infrastructure.database.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserServiceTest {

    @Autowired
    private IUserRepository userRepo;
    private final RedisTemplate<String, Object> redisTemplate;

    public UserServiceTest(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public UserEntity getAllUser(){
        // Set gia trị có key loda_list
        redisTemplate.opsForValue().set("test", userRepo.findByUsername("bigcream"));

        UserEntity user = (UserEntity) redisTemplate.opsForValue().get("test");
        return user;
    }
}
