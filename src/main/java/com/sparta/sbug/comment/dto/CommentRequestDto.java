package com.sparta.sbug.comment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

// lombok
@Getter
@NoArgsConstructor
public class CommentRequestDto {

    @NotNull(message = "댓글이 비어있습니다.")
    String content;

}
