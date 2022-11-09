package com.example.learn.domain.actor;

import com.example.learn.infrastructure.database.dto.Message;

public class ChatRoom {
    public static final class GetSession {}
    public static final class sendPrivateMessage  {
        public final Message message;
        public sendPrivateMessage(Message message) {
            this.message = message;
        }
    }
    public static final class SessionGranted  {}
    public static final class SessionDenied {
        public final String reason;

        public SessionDenied(String reason) {
            this.reason = reason;
        }
    }
    public static final class MessagePosted {
        public final String screenName;
        public final String message;

        public MessagePosted(String screenName, String message) {
            this.screenName = screenName;
            this.message = message;
        }
    }
    public static final class PostMessage{
        public final String message;

        public PostMessage(String message) {
            this.message = message;
        }
    }

    static final class NotifyClient{
        final MessagePosted message;
        public NotifyClient(MessagePosted message) {
            this.message = message;
        }
    }
}