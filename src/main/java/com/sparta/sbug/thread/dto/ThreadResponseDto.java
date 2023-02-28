package com.sparta.sbug.thread.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.sbug.emoji.dto.EmojiResponseDto;
import com.sparta.sbug.thread.entity.Thread;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 쓰레드 반환 DTO
 */
// lombok
@Getter
@NoArgsConstructor
public class ThreadResponseDto {
    private Long threadId;
    private String userNickname;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<EmojiResponseDto> emojis;

    private ThreadResponseDto(Thread thread, Map<Long, List<EmojiResponseDto>> threadEmojiCountMap) {
        this.threadId = thread.getId();
        this.userNickname = thread.getUser().getNickname(); // 이름으로 넣을지 확인
        this.userId = thread.getUser().getId();
        this.content = thread.getContent();
        this.createdAt = thread.getCreatedAt();
        this.modifiedAt = thread.getModifiedAt();
        if (threadEmojiCountMap != null) {
            this.emojis = threadEmojiCountMap.get(thread.getId());
        } else {
            this.emojis = new ArrayList<>();
        }
        // 이부분에서 LazyLoading 발생. BatchSize 만큼의 in 절 발생.
    }

    @QueryProjection
    public ThreadResponseDto(Long threadId, String userNickname, Long userId, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.threadId = threadId;
        this.userNickname = userNickname;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static ThreadResponseDto of(Thread thread, Map<Long, List<EmojiResponseDto>> threadEmojiCountMap) {
        return new ThreadResponseDto(thread, threadEmojiCountMap);
    }

    public void setEmojis(List<EmojiResponseDto> emojis) {
        this.emojis = emojis;
    }

}
