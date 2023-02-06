package com.sparta.sbug.emoji.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Thread")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThreadEmoji extends Emoji{
}
