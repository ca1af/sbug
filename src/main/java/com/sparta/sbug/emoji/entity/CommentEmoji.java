package com.sparta.sbug.emoji.entity;

import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@DiscriminatorValue("COMMENT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentEmoji extends Emoji{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public CommentEmoji(String emojiType, User user, Comment comment){
        super(emojiType, user);
        this.comment = comment;
    }
}
