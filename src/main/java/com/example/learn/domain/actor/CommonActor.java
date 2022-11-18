package com.example.learn.domain.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import com.example.learn.application.GreetingService;
import com.example.learn.infrastructure.database.dto.ChatMessage;
import com.example.learn.infrastructure.database.dto.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class CommonActor extends AbstractActor {
    private ActorRef sender;
    private final GreetingService greetingService;

    public CommonActor(GreetingService greetingService) {
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
        return msg.getMessage();
    }
}
