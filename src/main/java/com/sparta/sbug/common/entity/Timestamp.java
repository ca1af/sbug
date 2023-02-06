package com.sparta.sbug.common.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// lombok
@Getter

// jpa
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Timestamp {
    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    /**
    * 생성 시간을 기록하는 메소드
    * */
    public void recordCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * 수정 시간을 갱신하는 메소드
     */
    public void updateModifiedAt() {
        this.modifiedAt = LocalDateTime.now();
    }
}
