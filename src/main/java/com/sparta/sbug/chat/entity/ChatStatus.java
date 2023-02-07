package com.sparta.sbug.chat.entity;

public enum ChatStatus {
    READ(Status.READ),
    NEW(Status.NEW);

    private final String status;

    ChatStatus(String status) {
        this.status = status;
    }

    public static class Status {
        public static final String READ = "READ";
        public static final String NEW = "NEW";
    }

    public String toString() {
        return this.status;
    }
}
