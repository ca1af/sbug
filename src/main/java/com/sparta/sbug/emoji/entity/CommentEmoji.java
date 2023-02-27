package com.sparta.sbug.emoji.entity;

import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// lombok
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

// jpa
@Entity
@DiscriminatorValue("COMMENT")
public class CommentEmoji extends Emoji {

    /**
     * 생성자
     */
    public CommentEmoji(String emojiType, User user, Comment comment) {
        super(emojiType, user);
        this.comment = comment;
    }

    /**
     * 연관관계
     * comment_emoji : comment = N:1 양방향 연관 관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    /**
     * 연관관계 편의 메소드
     * setComment : comment_emoji - comment
     */
    public void setComment(Comment comment) {
        this.comment = comment;
        comment.addEmoji(this);
    }

}
