package com.sparta.sbug.emoji.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.sbug.emoji.entity.EmojiType;
import lombok.Getter;
import lombok.NoArgsConstructor;

// lombok
@Getter
@NoArgsConstructor
public class EmojiCountDto {
    private Long id;
    private EmojiType emojiType;
    private Long count;

    @QueryProjection
    public EmojiCountDto(Long id, EmojiType emojiType, Long count) {
        this.id = id;
        this.emojiType = emojiType;
        this.count = count;
    }
}
