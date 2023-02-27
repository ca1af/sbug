package com.sparta.sbug.emoji.dto;

import com.sparta.sbug.emoji.entity.EmojiType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 이모지 반응 요청 DTO
 */
// lombok
@Getter
@NoArgsConstructor
public class EmojiResponseDto {
    private EmojiType emojiType;
    private Long count;

    private EmojiResponseDto(Emoji emoji) {
        this.emojiType = emoji.getEmojiType();
        this.userId = emoji.getUser().getId();
    }

    public static EmojiResponseDto of(EmojiType emojiType, Long count) {
        return new EmojiResponseDto(emojiType, count);
    }

    public static Map<Long, List<EmojiResponseDto>> getEmojiCountMap(List<EmojiCountDto> emojiCountDtoList) {
        Map<Long, List<EmojiResponseDto>> emojiCountMap = new HashMap<>();
        for (EmojiCountDto emojiCountDto : emojiCountDtoList) {
            Long id = emojiCountDto.getId();
            if (emojiCountMap.containsKey(id)) {
                emojiCountMap.get(id)
                        .add(EmojiResponseDto.of(emojiCountDto.getEmojiType(), emojiCountDto.getCount()));
            } else {
                List<EmojiResponseDto> dtoList = new ArrayList<>();
                dtoList.add(EmojiResponseDto.of(emojiCountDto.getEmojiType(), emojiCountDto.getCount()));
                emojiCountMap.put(id, dtoList);
            }
        }

        return emojiCountMap;
    }
}


