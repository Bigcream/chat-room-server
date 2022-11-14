package com.example.learn.application;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.example.learn.domain.chatroom.ChatRoom;
import com.example.learn.infrastructure.UtilityActor;
import com.example.learn.infrastructure.constant.ActorName;
import com.example.learn.infrastructure.database.dto.Message;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
@AllArgsConstructor
public class ChatRoomService {
    private final ActorSystem actorSystem;
    private static final HashMap<Long, Long> chatRoomIdMap = new HashMap<>();

    public String createRoom(Message message) throws Exception {
        message.setRoomId(genChatRoomId());
        ActorRef chatRoomActor = UtilityActor.getInstanceOfActor(message.getRoomId().toString(), actorSystem, ActorName.CHAT_ROOM_ACTOR);
//        chatRoomActor.tell(new ChatRoom.CreateRoom(), chatRoomActor);
        System.out.println("create chat room " + chatRoomActor.path());
        System.out.println("RoomId: " + message.getRoomId());
        return UtilityActor.ask(chatRoomActor, new ChatRoom.CreateRoom(message), String.class);
    }
    private Long genChatRoomId(){
        Random rnd = new Random();
        int number = 0;
        boolean check = false;
        while (!check){
            number = rnd.nextInt(999999999);
            if (!chatRoomIdMap.containsKey((long) number)){
                chatRoomIdMap.put((long) number, (long) number);
                check = true;
            }
        }
        return (long) number;
    }
}
