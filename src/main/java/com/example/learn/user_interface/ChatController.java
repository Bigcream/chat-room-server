package com.example.learn.user_interface;


import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.util.Timeout;
import com.example.learn.domain.actor.GreetingActor;
import com.example.learn.infrastructure.database.dto.Message;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.example.learn.domain.actor.SpringExtension.SPRING_EXTENSION_PROVIDER;

@Controller
@AllArgsConstructor
public class ChatController {
    @Autowired
    private ActorRef actor;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message){
        actor.tell(new GreetingActor.Greet("john public"), actor);
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message){
        actor.tell(new GreetingActor.Greet("john private"), actor);
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
        System.out.println(message.toString());
        return message;
    }
}
