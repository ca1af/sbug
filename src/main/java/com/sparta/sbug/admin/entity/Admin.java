package com.sparta.sbug.admin.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin {
    @Id
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    @Enumerated(value = EnumType.STRING)
    private AdminRole role = AdminRole.ADMIN;
    @Builder
    public Admin(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
