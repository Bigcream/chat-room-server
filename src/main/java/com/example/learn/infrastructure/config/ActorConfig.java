package com.example.learn.infrastructure.config;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import com.example.learn.domain.actor.ChatRoom;
import com.example.learn.domain.actor.Gabbler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class ActorConfig {

    @Bean
    public ActorSystem setUpActor(){
        return ActorSystem.create(create(), "ChatRoomDemo");
    }
    public static Behavior<Void> create() {
        return Behaviors.setup(
                context -> {
                    ActorRef<ChatRoom.RoomCommand> chatRoom = context.spawn(ChatRoom.create(), "chatRoom");
                    ActorRef<ChatRoom.SessionEvent> gabbler = context.spawn(Gabbler.create(), "gabbler");
                    context.watch(gabbler);
                    chatRoom.tell(new ChatRoom.GetSession("ol’ Gabbler", gabbler));

                    return Behaviors.receive(Void.class)
                            .onSignal(Terminated.class, sig -> Behaviors.stopped())
                            .build();
                });
    }


}
