package com.example.learn.domain.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

class SessionBehavior extends AbstractBehavior<ChatRoom.SessionCommand> {
    private final ActorRef<ChatRoom.RoomCommand> room;
    private final String screenName;
    private final ActorRef<ChatRoom.SessionEvent> client;

    public static Behavior<ChatRoom.SessionCommand> create(
            ActorRef<ChatRoom.RoomCommand> room, String screenName, ActorRef<ChatRoom.SessionEvent> client) {
        return Behaviors.setup(context -> new SessionBehavior(context, room, screenName, client));
    }

    private SessionBehavior(
            ActorContext<ChatRoom.SessionCommand> context,
            ActorRef<ChatRoom.RoomCommand> room,
            String screenName,
            ActorRef<ChatRoom.SessionEvent> client) {
        super(context);
        this.room = room;
        this.screenName = screenName;
        this.client = client;
    }

    @Override
    public Receive<ChatRoom.SessionCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(ChatRoom.PostMessage.class, this::onPostMessage)
                .onMessage(ChatRoom.NotifyClient.class, this::onNotifyClient)
                .build();
    }

    private Behavior<ChatRoom.SessionCommand> onPostMessage(ChatRoom.PostMessage post) {
        // from client, publish to others via the room
        room.tell(new ChatRoom.PublishSessionMessage(screenName, post.message));
        return Behaviors.same();
    }

    private Behavior<ChatRoom.SessionCommand> onNotifyClient(ChatRoom.NotifyClient notification) {
        // published from the room
        client.tell(notification.message);
        return Behaviors.same();
    }
}