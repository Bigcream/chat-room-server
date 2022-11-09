package com.example.learn.domain.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.japi.pf.ReceiveBuilder;
import com.example.learn.application.GreetingService;
import com.example.learn.infrastructure.UtilityActor;
import com.example.learn.infrastructure.database.dto.ChatMessage;
import com.example.learn.infrastructure.database.dto.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class UserActor extends AbstractActor {
    private ActorRef sender;
    private final GreetingService greetingService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public UserActor(GreetingService greetingService) {
        this.greetingService = greetingService;
    }


    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(ChatMessage.class, msg -> {
                    sender = sender();
                    Message response = chat(msg);
                    sender.tell(response, self());
                })
                .build();
    }
    private Message chat(ChatMessage msg) {
        simpMessagingTemplate.convertAndSend("/chatroom/public", msg.getMessage());
        return msg.getMessage();
    }
}
