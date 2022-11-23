package com.example.learn.application;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.example.learn.domain.actor.chatroom.ChatRoom;
import com.example.learn.infrastructure.utility.UtilityActor;
import com.example.learn.infrastructure.constant.ActorName;
import com.example.learn.infrastructure.database.dto.ChatMessage;
import com.example.learn.infrastructure.database.dto.Message;
import com.example.learn.infrastructure.database.entity.ChatRoomEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class UserChatService {
    private final ActorSystem actorSystem;
    @Autowired
    private HashMap<Long, List<ChatRoomEntity>> chatRoomMap;
    public void joinRoom(Message message) throws Exception {
        ActorRef userActor = UtilityActor.getInstanceOfActor(message.getSenderName(), actorSystem, ActorName.USER_ACTOR);
        userActor.tell(new ChatMessage(message), userActor);
        System.out.println("join " + userActor.path());
    }
    public void sendPublicChat(Message message) throws Exception {
        ActorRef userActor = UtilityActor.getInstanceOfActor(message.getSenderName(), actorSystem, ActorName.USER_ACTOR);
        userActor.tell(new ChatMessage(message), userActor);
        System.out.println("test " + userActor.path());
    }
    public void sendPrivateChat(Message message) throws Exception{
        ActorRef userActor = UtilityActor.getInstanceOfActor(message.getSenderName(), actorSystem, ActorName.USER_ACTOR);
        userActor.tell(new ChatRoom.SendPrivateChat(message), userActor);
    }
}
