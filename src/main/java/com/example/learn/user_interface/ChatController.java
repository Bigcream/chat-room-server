package com.example.learn.user_interface;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import com.example.learn.domain.actor.ChatRoom;
import com.example.learn.infrastructure.config.ActorConfig;
import com.example.learn.infrastructure.database.dto.Message;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class ChatController {


    @Autowired
    private ActorSystem<ChatRoom.RoomCommand> chatRoom;
    @Autowired
    private ActorSystem<ChatRoom.SessionEvent> gabbler;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message){
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message){
        chatRoom.tell(new ChatRoom.GetSession("ol’ Gabbler", gabbler));
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
        System.out.println(message.toString());
        return message;
    }
}
