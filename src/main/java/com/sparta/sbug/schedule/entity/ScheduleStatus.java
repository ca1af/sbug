package com.sparta.sbug.schedule.entity;

/**
 * 일정의 상태를 나타내는 열거형
 * DONE: 일정이 이미 완료된 상태
 * UNDONE: 일정이 아직 완료되지 않은 상태
 */
public enum ScheduleStatus {

    DONE(Status.DONE),
    UNDONE(Status.UNDONE);

    private final String status;

    ScheduleStatus(String status) {
        this.status = status;
    }

    /**
     * 일정 상태를 문자열로 반환하는 메서드
     *
     * @return String
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * 일정 상태를 문자열로 저장하는 내부 클래스
     */
    public static class Status {
        public static final String DONE = "STATUS_DONE";
        public static final String UNDONE = "STATUS_UNDONE";
    }
}
