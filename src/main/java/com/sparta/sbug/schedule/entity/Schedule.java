package com.sparta.sbug.schedule.entity;

import com.sparta.sbug.common.entity.Timestamp;
import com.sparta.sbug.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Schedule extends Timestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "user_id")
    private User user;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    @Enumerated
    private ScheduleStatus status;

    @Column
    private LocalDateTime doneAt;

    @Builder
    public Schedule(
        User user, String content, LocalDateTime date, ScheduleStatus status
    ) {
        this.user = user;
        this.content = content;
        this.date = date;
        this.status = status;
    }

    public void updateSchedule(
        String content, LocalDateTime date
    ) {
        this.content = content;
        this.date = date;
    }

    public void complete() {
        this.status = ScheduleStatus.DONE;
    }
    public void incomplete() {
        this.status = ScheduleStatus.UNDONE;
    }

}
