package com.example.learn.application;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.example.learn.domain.actor.chatroom.ChatRoom;
import com.example.learn.domain.api.user.IUserRepository;
import com.example.learn.infrastructure.database.entity.UserEntity;
import com.example.learn.infrastructure.utility.UtilityActor;
import com.example.learn.infrastructure.constant.ActorName;
import com.example.learn.infrastructure.database.dto.ChatMessage;
import com.example.learn.infrastructure.database.dto.Message;
import com.example.learn.infrastructure.database.entity.ChatRoomEntity;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class UserChatService {
    private final IUserRepository userRepo;
    private final ActorSystem actorSystem;
    private final HashMap<String, AtomicLong> mapId;
    private final PasswordEncoder passwordEncoder;
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
    public UserEntity register(UserEntity user) throws Exception {
        UserEntity user1 = new UserEntity();
        mapId.get("userId").getAndIncrement();
        ActorRef userActor = UtilityActor.getInstanceOfActor(user.getUsername(), actorSystem, ActorName.USER_ACTOR);
        Long id = mapId.get("userId").get();
        user.setId(id);
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("id: " + user1.getId());
        System.out.println("actor name: " + userActor.path());
        user1 = UtilityActor.ask(userActor, UserEntity.builder().id(user.getId()).username(user.getUsername()).password(user.getPassword()).build(), UserEntity.class);
        return user1;
    }
}
