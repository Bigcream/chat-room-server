package com.example.learn.domain.chatroom;

import com.example.learn.infrastructure.database.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
}
