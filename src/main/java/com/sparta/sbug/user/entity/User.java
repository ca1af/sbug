package com.sparta.sbug.user.entity;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.common.entity.Timestamp;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends Timestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole userRole = UserRole.USER;

    @Builder
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    @OneToMany(mappedBy = "user")
    Set<Channel> channels = new LinkedHashSet<>();

    public void updateUser(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public void setUserRole(UserRole role) {
        this.userRole = role;
    }

    //연관관계 편의 매서드
    public void addChannel(Channel channel) {
        this.channels.add(channel);
    }
}
