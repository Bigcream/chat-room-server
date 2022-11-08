package com.example.learn.user_interface;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import com.example.learn.infrastructure.UtilityActor;
import com.example.learn.infrastructure.database.dto.ChatMessage;
import com.example.learn.infrastructure.database.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
public class ChatController {

    @Autowired
    @Qualifier("greetingActor1")
    private ActorRef greetingActor1;
    @Autowired
    @Qualifier("userActor1")
    private ActorRef userActor1;
    @Autowired
    private ActorSystem actorSystem;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    public Message receiveMessage(@Payload Message message) throws Exception {
        userActor1.tell(new ChatMessage(message), userActor1);
        String actorPath = "akka://akka-spring-demo/user/" + message.getSenderName();
        ActorRef userActor = UtilityActor.getInstanceOfActor(message.getSenderName(), actorSystem);
        simpMessagingTemplate.convertAndSend("/chatroom/public", UtilityActor.ask(userActor, new ChatMessage(message), Message.class));
        System.out.println("test "+userActor.path());
        return new Message();
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message){
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
        System.out.println("greeting " + greetingActor1.path());
        return UtilityActor.ask(greetingActor1, new ChatMessage(message), Message.class);
    }

}
