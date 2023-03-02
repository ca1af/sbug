package com.sparta.sbug.common.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import com.fasterxml.jackson.datatype.jsr310.deser.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 생성, 수정 시간을 표기하기 위한 시간 표기(Timestamp) 객체
 */
// lombok
@Getter

// jpa
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Timestamp implements Serializable {

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @CreatedDate
    private LocalDateTime createdAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @LastModifiedDate
    private LocalDateTime modifiedAt;

}
