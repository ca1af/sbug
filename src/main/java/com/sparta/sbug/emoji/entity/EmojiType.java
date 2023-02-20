package com.sparta.sbug.emoji.entity;

public enum EmojiType {

    SMILE("SMILE"),
    CRY("CRY"),
    HEART("HEART"),
    LIKE("LIKE");

    private final String emojiType;

    /**
     * 생성자
     *
     * @param emojiType 이모지 반응 종류
     */
    EmojiType(String emojiType) {
        this.emojiType = emojiType;
    }

    /**
     * 이모지 반응을 문자열로 반환해주는 메서드
     * @return String
     */
    public String getEmojiType() {
        return this.emojiType;
    }
}
