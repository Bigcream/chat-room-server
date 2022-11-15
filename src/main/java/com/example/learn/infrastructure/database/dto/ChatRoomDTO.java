package com.example.learn.infrastructure.database.dto;

import com.example.learn.infrastructure.database.entity.UserEntity;
import lombok.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ChatRoomDTO {
    private Long chatRoomId;
    private UserEntity createBy;
    private Set<UserEntity> userInRooms;
}
