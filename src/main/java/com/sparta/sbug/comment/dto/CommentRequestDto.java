package com.sparta.sbug.comment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 댓글 요청 DTO
 */
// lombok
@Getter
@NoArgsConstructor
public class CommentRequestDto {

    @NotNull(message = "댓글이 비어있습니다.")
    String content;

}
