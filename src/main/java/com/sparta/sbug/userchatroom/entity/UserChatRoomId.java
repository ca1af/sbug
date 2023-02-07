package com.sparta.sbug.userchatroom.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

// lombok
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChatRoomId implements Serializable {
    private Long user;
    private Long chatRoom;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserChatRoomId userChatRoomId = (UserChatRoomId) o;

        return Objects.equals(getUser(), userChatRoomId.getUser()) && Objects.equals(getChatRoom(), userChatRoomId.getChatRoom());
    }

    @Override
    public int hashCode() {
        int result = getUser() != null ? getUser().hashCode() : 0;
        result = 31 * result + (getChatRoom() != null ? getChatRoom().hashCode() : 0);
        return result;
    }
}
