package com.sparta.sbug.schedule.entity;

import com.sparta.sbug.common.entity.Timestamp;
import com.sparta.sbug.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Setter;

import java.time.LocalDateTime;

// lombok
@Getter
@NoArgsConstructor

// jpa
@Entity
public class Schedule extends Timestamp {
    /**
     * 컬럼
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Setter
    private String content;

    @Column(nullable = false)
    @Setter
    private LocalDateTime date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ScheduleStatus status = ScheduleStatus.UNDONE;

    @Column
    private LocalDateTime doneAt;

    /**
     * 생성자
     *
     * @param user    유저
     * @param content 내용
     * @param date    완료 예정일
     */
    @Builder
    public Schedule(
        User user, String content, LocalDateTime date, ScheduleStatus status
    ) {
        this.user = user;
        this.content = content;
        this.date = date;
        this.status = status;
    }

    /**
     * 연관관계
     * schedule : user = N : 1 단방향 연관관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    /**
     * 서비스 메소드
     */
    public void updateSchedule(String content, LocalDateTime date) {
        this.content = content;
        this.date = date;
    }

    /**
     * 서비스 메소드
     */
    public void checkDoneSchedule() {
        this.status = ScheduleStatus.DONE;
        this.doneAt = LocalDateTime.now();
    }

    /**
     * 서비스 메소드
     */
    public void uncheckDoneSchedule() {
        this.status = ScheduleStatus.UNDONE;
    }
}
