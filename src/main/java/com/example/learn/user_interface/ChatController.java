package com.example.learn.user_interface;


import com.example.learn.application.ChatRoomService;
import com.example.learn.application.UserChatService;
import com.example.learn.infrastructure.database.dto.ChatRoomDTO;
import com.example.learn.infrastructure.database.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ChatController extends BaseController {
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private UserChatService userChatService;

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
        return chatRoomService.getAllRoomAvailable();
    }
    @GetMapping("/leave-room/{roomId}")
    public ResponseEntity<Void> leaveRoom(@PathVariable Long roomId, @RequestParam String username){

        return new  ResponseEntity<>(chatRoomService.leaveRoom(roomId, username), noCacheHeader, HttpStatus.OK);
    }
}
