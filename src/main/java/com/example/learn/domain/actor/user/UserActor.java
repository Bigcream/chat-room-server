package com.example.learn.domain.actor.user;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import com.example.learn.application.GreetingService;
import com.example.learn.domain.actor.chatroom.ChatRoom;
import com.example.learn.domain.api.user.IUserRepository;
import com.example.learn.infrastructure.database.dto.ChatMessage;
import com.example.learn.infrastructure.database.dto.Message;
import com.example.learn.infrastructure.database.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class UserActor extends AbstractActor {
    private ActorRef sender;
    private final GreetingService greetingService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private IUserRepository userRepo;
    public UserActor(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(ChatMessage.class, msg -> {
                    sender = sender();
                    Message response = chat(msg);
                    sender.tell(response, self());
                })
                .match(ChatRoom.SendPrivateChat.class, msg ->{
                    sender = sender();
                    Message response = sendToUser(msg.message);
                })
                .match(UserEntity.class, user -> {
                    sender = sender();
                    UserEntity response = saveUser(user);
                    sender.tell(response, self());
                })
                .build();
    }
    private Message chat(ChatMessage msg) {
        simpMessagingTemplate.convertAndSend("/chatroom/public", msg.getMessage());
        return msg.getMessage();
    }

    private Message sendToUser(Message msg) {
        simpMessagingTemplate.convertAndSendToUser(msg.getReceiverName(),"/private",msg);
        System.out.println("sender private " + sender.path());
        return msg;
    }
    private UserEntity saveUser(UserEntity user){
        return userRepo.save(user);
    }
}
