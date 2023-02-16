package com.sparta.sbug.comment.dto;

import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.emoji.entity.CommentEmoji;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 댓글 응답 DTO
 */
// lombok
@Getter
@RequiredArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long userId;
    private String username;
    private final List<String> usersOfEmoji1 = new ArrayList<>();
    private final List<String> usersOfEmoji2 = new ArrayList<>();
    private final List<String> usersOfEmoji3 = new ArrayList<>();
    private final List<String> usersOfEmoji4 = new ArrayList<>();

    private CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.userId = comment.getUser().getId();
        this.username = comment.getUser().getNickname();

        Set<CommentEmoji> emojis = comment.getEmojis();
        for (CommentEmoji emoji : emojis) {
            switch (emoji.getEmojiType()) {
                case Emoji1 -> usersOfEmoji1.add(emoji.getUser().getNickname());
                case Emoji2 -> usersOfEmoji2.add(emoji.getUser().getNickname());
                case Emoji3 -> usersOfEmoji3.add(emoji.getUser().getNickname());
                case Emoji4 -> usersOfEmoji4.add(emoji.getUser().getNickname());
            }
        }
    }

    public static CommentResponseDto of(Comment comment) {
        return new CommentResponseDto(comment);
    }

}
