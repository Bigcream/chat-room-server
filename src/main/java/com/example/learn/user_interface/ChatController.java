package com.example.learn.user_interface;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.example.learn.application.ChatRoomService;
import com.example.learn.application.UserService;
import com.example.learn.infrastructure.UtilityActor;
import com.example.learn.infrastructure.database.dto.ChatMessage;
import com.example.learn.infrastructure.database.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

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
    private ChatRoomService chatRoomService;
    @Autowired
    private UserService userService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    public Message receiveMessage(@Payload Message message) throws Exception {
        userService.sendPublicChat(message);
        return message;
    }

    @MessageMapping("/join-room")
    public Message userJoinRoom(@Payload Message message) throws Exception {
        userService.joinRoom(message);
        return message;
    }

    @MessageMapping("/create-room")
    public Message userCreateRoom(@Payload Message message) throws Exception {
        chatRoomService.createRoom(message);
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message){
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
        System.out.println("greeting " + greetingActor1.path());
        return UtilityActor.ask(greetingActor1, new ChatMessage(message), Message.class);
    }

}
