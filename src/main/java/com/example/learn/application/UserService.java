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
public class UserService {
    private final ActorSystem actorSystem;
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
        userActor.tell(new ChatMessage(message), userActor);
    }
}
