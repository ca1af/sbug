package com.sparta.sbug.chat.entity;

/**
 * 채팅의 상태를 나타내는 열거형
 * READ = 읽음
 * NEW = 읽지 않음
 */
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
