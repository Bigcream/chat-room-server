package com.example.learn.user_interface;


import com.example.learn.application.ChatRoomService;
import com.example.learn.application.UserChatService;
import com.example.learn.infrastructure.database.dto.ChatRoomDTO;
import com.example.learn.infrastructure.database.dto.Message;
import com.example.learn.infrastructure.database.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.USER_HEADER;

@RestController
@RequestMapping("/api/v1")
public class ChatController extends BaseController {
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private UserChatService userChatService;
    static Set<Session> users = Collections.synchronizedSet(new HashSet<>());
    @OnOpen
    public void handleOpen(Session session) throws IOException {
        session.getBasicRemote().sendText("You are connected as: " + session.getId());
        System.out.println("You are connected as: " + session.getId());
        users.add(session);
        sendListUserOnline();
    }

    public void sendListUserOnline() throws IOException {
        for (Session session : users) {
            session.getBasicRemote().sendText(buildListUser());
        }
    }
    public String buildListUser() {
        StringBuffer listUser = new StringBuffer("list_user");
        for (Session session : users) {
            listUser.append(session.getId() + " \n");
        }
        return listUser.toString();
    }

    @MessageMapping("/message")
    public Message receiveMessage(@Payload Message message) throws Exception {
        userChatService.sendPublicChat(message);
        return message;
    }

    @MessageMapping("/join-room")
    public Message userJoinRoom(StompHeaderAccessor sha, @Payload Message message, Principal principal) throws Exception {
        System.out.println("user: " + principal.getName());
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
    @GetMapping("/get-all-user")
    public ResponseEntity<List<String>> getAllUser(){
        return new  ResponseEntity<>(chatRoomService.getAllUser(), noCacheHeader, HttpStatus.OK);
    }
}
