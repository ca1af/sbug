package com.sparta.sbug.emoji.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentEmoji extends Emoji{
}
