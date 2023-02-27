package com.sparta.sbug.thread.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.sbug.emoji.dto.EmojiResponseDto;
import com.sparta.sbug.thread.entity.Thread;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    private ThreadResponseDto(Thread thread) {
        this.threadId = thread.getId();
        this.userNickname = thread.getUser().getNickname(); // 이름으로 넣을지 확인
        this.userId = thread.getUser().getId();
        this.content = thread.getContent();
        this.createdAt = thread.getCreatedAt();
        this.modifiedAt = thread.getModifiedAt();
        this.emojis = thread.getEmojis().stream().map(EmojiResponseDto::of).collect(Collectors.toList());
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

    /**
     * 쓰레드 객체를 받아 응답 DTO로 반환하는 메서드
     *
     * @param thread 대상 객체
     */
    public static ThreadResponseDto of(Thread thread) {
        return new ThreadResponseDto(thread);
    }

    public void setEmojis(List<EmojiResponseDto> emojis) {
        this.emojis = emojis;
    }

}
