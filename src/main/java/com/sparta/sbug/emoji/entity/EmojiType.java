package com.sparta.sbug.emoji.entity;

public enum EmojiType {
    Emoji1("Emoji1"), Emoji2("Emoji2"), Emoji3("Emoji3"), Emoji4("Emoji4");

    private final String emojiType;

    EmojiType(String emojiType) {this.emojiType = emojiType;}

    public String getEmojiType() {
        return this.emojiType;
    }
}
