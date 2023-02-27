package com.sparta.sbug.comment.dto;

import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.emoji.dto.EmojiResponseDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 댓글 응답 DTO
 */
// lombok
@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
    private Long userId;
    private String userNickname;
    private List<EmojiResponseDto> emojis;

    private CommentResponseDto(Comment comment, Map<Long, List<EmojiResponseDto>> commentEmojiCountMap) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.userId = comment.getUser().getId();
        this.userNickname = comment.getUser().getNickname();
        if (commentEmojiCountMap != null) {
            this.emojis = commentEmojiCountMap.get(comment.getId());
        } else {
            this.emojis = new ArrayList<>();
        }
    }

    public static CommentResponseDto of(Comment comment, Map<Long, List<EmojiResponseDto>> commentEmojiCountMap) {
        return new CommentResponseDto(comment, commentEmojiCountMap);
    }

}
