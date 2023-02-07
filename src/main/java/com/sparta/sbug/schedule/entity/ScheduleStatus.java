package com.sparta.sbug.schedule.entity;

public enum ScheduleStatus {
    DONE(Status.DONE),
    UNDONE(Status.UNDONE);

    private final String status;

    ScheduleStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return  this.status;
    }

    public static class Status{
        public static final String DONE = "STATUS_DONE";
        public static final String UNDONE = "STATUS_UNDONE";
    }
}
