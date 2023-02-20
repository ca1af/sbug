package com.sparta.sbug.comment.dto;

import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.emoji.dto.EmojiResponseDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 댓글 응답 DTO
 */
// lombok
@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long userId;
    private String userNickname;
    private List<EmojiResponseDto> emojis;

    private CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.userId = comment.getUser().getId();
        this.userNickname = comment.getUser().getNickname();
        this.emojis = comment.getEmojis().stream().map(EmojiResponseDto::of).collect(Collectors.toList());
    }

    public static CommentResponseDto of(Comment comment) {
        return new CommentResponseDto(comment);
    }

}
