package com.sparta.sbug.thread.dto;

import com.sparta.sbug.thread.entity.Thread;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ThreadResponseDto {
    private Long threadId;
    private String userNickname;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public ThreadResponseDto(Thread thread){
        this.threadId = thread.getId();
        this.userNickname = thread.getUser().getNickname(); // 이름으로 넣을지 확인
        this.content = thread.getContent();
        this.createdAt = thread.getCreatedAt();
        this.modifiedAt = thread.getModifiedAt();
    }

}
