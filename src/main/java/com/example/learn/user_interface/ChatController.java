package com.example.learn.user_interface;


import akka.actor.ActorRef;
import akka.pattern.Patterns;
import com.example.learn.domain.actor.GreetingActor;
import com.example.learn.domain.actor.TestActor;
import com.example.learn.infrastructure.database.dto.ChatMessage;
import com.example.learn.infrastructure.database.dto.Message;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Duration;

@Controller
public class ChatController {

    @Autowired
    @Qualifier("greetingActor1")
    private ActorRef greetingActor1;

    @Autowired
    @Qualifier("testActor1")
    private ActorRef testActor1;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message){
        testActor1.tell(new TestActor.Greet("john public"), testActor1);
        System.out.println("test"+testActor1.path());
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message){
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
        System.out.println("greeting " + greetingActor1.path());
        return ask(greetingActor1, new ChatMessage(message), Message.class);
    }
    private <T> T ask(ActorRef actor, Object msg, Class<T> returnTypeClass){
        return  (T) Patterns.ask(actor, msg, Duration.ofMillis(2000)).toCompletableFuture().join();
    }

}
