package com.example.learn.application;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.example.learn.infrastructure.UtilityActor;
import com.example.learn.infrastructure.constant.ActorName;
import com.example.learn.infrastructure.database.dto.ChatMessage;
import com.example.learn.infrastructure.database.dto.Message;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatRoomService {
    private final ActorSystem actorSystem;

    public void createRoom(Message message) throws Exception {
        ActorRef chatRoomActor = UtilityActor.getInstanceOfActor(message.getRoomId().toString(), actorSystem, ActorName.CHAT_ROOM_ACTOR);
        chatRoomActor.tell(new ChatMessage(message), chatRoomActor);
        System.out.println("create chat room " + chatRoomActor.path());
        System.out.println("RoomId: " + message.getRoomId());
    }
}
