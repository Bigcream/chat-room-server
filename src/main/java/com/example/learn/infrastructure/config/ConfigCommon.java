package com.example.learn.infrastructure.config;

import com.example.learn.infrastructure.database.entity.ChatRoomEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;

@Configuration
public class ConfigCommon {

    @Bean(name = "chatRoomMap")
    public HashMap<Long, List<ChatRoomEntity>> getChatRoomMap(){
        return new HashMap<>();
    }
}
