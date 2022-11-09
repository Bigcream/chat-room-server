package com.example.learn.domain.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import com.example.learn.application.GreetingService;
import com.example.learn.infrastructure.database.dto.ChatMessage;
import com.example.learn.infrastructure.database.dto.Message;
import com.example.learn.infrastructure.database.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class ChatRoomActor extends AbstractActor {
    private ActorRef sender;
    private final GreetingService greetingService;

    private final RedisTemplate<String, Object> redisTemplate;

    public ChatRoomActor(GreetingService greetingService, RedisTemplate<String, Object> redisTemplate) {
        this.greetingService = greetingService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(ChatMessage.class, msg -> {
                    sender = sender();
                    Message response = chat(msg);
                    sender.tell(response, self());
                    redisTemplate.opsForValue().set(response.getRoomId().toString(), response);
                })
                .build();
    }
    private Message chat(ChatMessage msg) {
        return msg.getMessage();
    }
}

