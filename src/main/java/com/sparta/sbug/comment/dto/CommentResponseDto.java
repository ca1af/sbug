package com.sparta.sbug.comment.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// lombok
@Getter
@RequiredArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long userId;
    private String username;
    private List<String> usersOfEmoji1 = new ArrayList<>();
    private List<String> usersOfEmoji2 = new ArrayList<>();
    private List<String> usersOfEmoji3 = new ArrayList<>();
    private List<String> usersOfEmoji4 = new ArrayList<>();

    @Builder
    public CommentResponseDto(Long id, String content, LocalDateTime createdAt, Long userId, String username,
                              List<String> usersOfEmoji1, List<String> usersOfEmoji2, List<String> usersOfEmoji3,
                              List<String> usersOfEmoji4) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.userId = userId;
        this.username = username;
    }

    public void addUserToEmoji1(String username) {
        usersOfEmoji1.add(username);
    }

    public void addUserToEmoji2(String username) {
        usersOfEmoji2.add(username);
    }

    public void addUserToEmoji3(String username) {
        usersOfEmoji3.add(username);
    }

    public void addUserToEmoji4(String username) {
        usersOfEmoji4.add(username);
    }

}
