package com.example.learn.application;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.example.learn.domain.chatroom.ChatRoom;
import com.example.learn.infrastructure.UtilityActor;
import com.example.learn.infrastructure.constant.ActorName;
import com.example.learn.infrastructure.database.dto.ChatRoomDTO;
import com.example.learn.infrastructure.database.dto.Message;
import com.example.learn.infrastructure.database.entity.ChatRoomEntity;
import com.example.learn.infrastructure.database.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ChatRoomService {
    private final ActorSystem actorSystem;
    @Autowired
    private HashMap<Long, List<ChatRoomEntity>> chatRoomMap;

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    public String createRoom(Message message) throws Exception {
        message.setRoomId(genChatRoomId(message.getSenderName()));
        ActorRef chatRoomActor = UtilityActor.getInstanceOfActor(message.getRoomId().toString(), actorSystem, ActorName.CHAT_ROOM_ACTOR);
//        chatRoomActor.tell(new ChatRoom.CreateRoom(), chatRoomActor);
        System.out.println("create chat room " + chatRoomActor.path());
        System.out.println("RoomId: " + message.getRoomId());
        return UtilityActor.ask(chatRoomActor, new ChatRoom.CreateRoom(message), String.class);
    }
    private Long genChatRoomId(String username){
        Random rnd = new Random();
        ChatRoomEntity chatRoom = new ChatRoomEntity();
        UserEntity user = new UserEntity();
        user.setUsername(username);
        List<ChatRoomEntity> chatRooms = new ArrayList<>();
        Set<UserEntity> userEntitySet = new HashSet<>();
        userEntitySet.add(user);
        int number = 0;
        boolean check = false;
        while (!check){
            number = rnd.nextInt(999999999);
            if (!chatRoomMap.containsKey((long) number)){
                chatRoom.setChatRoomId((long)number);
                chatRoom.setCreatedBy(user);
                chatRoom.setUsers(userEntitySet);
                chatRooms.add(chatRoom);
                chatRoomMap.put((long) number, chatRooms);
                check = true;
            }
        }
        return (long) number;
    }
    public List<ChatRoomDTO> getAllRoomAvailable(){
        List<ChatRoomDTO> chatRoomDTOS = new ArrayList<>();
        chatRoomMap.forEach((aLong, chatRoomEntities) -> chatRoomEntities.forEach(chatRoom ->{
            chatRoomDTOS.add(chatRoom.convertToDTO());
        }));
        return chatRoomDTOS;
    }
    public Void leaveRoom(Long roomId, String username){
        chatRoomMap.forEach((aLong, chatRoomEntities) -> {
            if(aLong.equals(roomId)){
                chatRoomEntities.forEach(chatRoomEntity -> {chatRoomEntity.getUsers().removeIf(
                        userEntity -> userEntity.getUsername().equals(username)
                );});
            }
        });
        return null;
    }
    public List<String> getAllUser(){
        List<String> usernames = new ArrayList<>();
        System.out.println(simpUserRegistry.getUserCount());
        Set<SimpUser> simpUsers = simpUserRegistry.getUsers();
        simpUsers.forEach(simpUser -> usernames.add(simpUser.getName()));
        return usernames;
    }
}
