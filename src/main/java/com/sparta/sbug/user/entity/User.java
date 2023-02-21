package com.sparta.sbug.user.entity;

import com.sparta.sbug.common.entity.Timestamp;
import jakarta.persistence.*;
import lombok.*;

// lombok
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

// jpa
@Entity
@Table(name = "users")
public class User extends Timestamp {
    /**
     * 컬럼
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Setter
    private boolean inUse = true;

    private Long kakaoId;

    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Column(nullable = false)
    private String password;

    @Setter
    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole userRole = UserRole.USER;

    /**
     * 생성자
     */
    @Builder
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    /**
     * 서비스 메소드
     */
    public void updateUser(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }

    public void setUserRole(UserRole role) {
        this.userRole = role;
    }

}
