package com.example.learn.user_interface;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.example.learn.application.ChatRoomService;
import com.example.learn.application.UserChatService;
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
    private UserChatService userChatService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    public Message receiveMessage(@Payload Message message) throws Exception {
        userChatService.sendPublicChat(message);
        return message;
    }

    @MessageMapping("/join-room")
    public Message userJoinRoom(@Payload Message message) throws Exception {
        userChatService.joinRoom(message);
        return message;
    }

    @MessageMapping("/create-room")
    public Message userCreateRoom(@Payload Message message) throws Exception {
        chatRoomService.createRoom(message);
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message) throws Exception {
        userChatService.sendPrivateChat(message);
        return message;
    }

}
