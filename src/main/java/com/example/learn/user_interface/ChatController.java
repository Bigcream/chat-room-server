package com.example.learn.user_interface;


import akka.actor.ActorRef;
import com.example.learn.application.ChatRoomService;
import com.example.learn.application.UserChatService;
import com.example.learn.domain.chatroom.ChatRoom;
import com.example.learn.infrastructure.UtilityActor;
import com.example.learn.infrastructure.database.dto.ChatRoomDTO;
import com.example.learn.infrastructure.database.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChatController extends BaseController {
    private final ActorRef actorCommon;
    private final ChatRoomService chatRoomService;
    private final UserChatService userChatService;

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
    public String userCreateRoom(@Payload Message message) throws Exception {
        return chatRoomService.createRoom(message);
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message) throws Exception {
        userChatService.sendPrivateChat(message);
        return message;
    }
    @GetMapping("/room-available")
    public List<ChatRoomDTO> getAllRoomAvailable(){
        return UtilityActor.askObject(actorCommon, new ChatRoom.GetAllRoomAvailable(), ChatRoomDTO.class);
    }
    @GetMapping("/leave-room/{roomId}")
    public ResponseEntity<Void> leaveRoom(@PathVariable Long roomId, @RequestParam String username){
        return new  ResponseEntity<>(chatRoomService.leaveRoom(roomId, username), noCacheHeader, HttpStatus.OK);
    }
    @GetMapping("/all-user-online")
    public List<String> getAllUserOnline(){
        return UtilityActor.askObject(actorCommon, new ChatRoom.GetAllUserOnline(), String.class);
    }
}
