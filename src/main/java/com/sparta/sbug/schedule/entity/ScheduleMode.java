package com.sparta.sbug.schedule.entity;

/**
 * 일정 entity의 Mode를 나타내는 열거형
 * NORMAL: 일반 일정관리용 Schedule
 * STUDYPLAN: 공부계획용 Schedule
 * REVIEW: 복습 알림 Schedule
 */
public enum ScheduleMode {

    NORMAL(Mode.NORMAL),
    STUDYPLAN(Mode.STUDYPLAN),
    REVIEW(Mode.REVIEW);

    private final String mode;

    ScheduleMode(String mode) {
        this.mode = mode;
    }
    
    /**
     * 일정 Mode를 문자열로 반환하는 메서드
     *
     * @return String
     */
    public String getMode() {
        return this.mode;
    }

    /**
     * 일정 Mode를 문자열로 저장하는 내부 클래스
     */
    public static class Mode {
        public static final String normal = "MODE_NORMAL"
        public static final String studyplan = "MODE_STUDYPLAN"
        public static final String review = "MODE_REVIEW"
    }
}
