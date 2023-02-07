package com.sparta.sbug.comment.entity;

import com.sparta.sbug.common.entity.Timestamp;
import com.sparta.sbug.emoji.entity.CommentEmoji;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends Timestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "comment")
    private Set<CommentEmoji> emojis = new LinkedHashSet<>();
}
