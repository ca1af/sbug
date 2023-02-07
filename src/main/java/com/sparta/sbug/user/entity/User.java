package com.sparta.sbug.user.entity;

<<<<<<< HEAD
import com.sparta.sbug.userchatroom.entity.UserChatRoom;
=======
import com.sparta.sbug.common.entity.Timestamp;
>>>>>>> 77e04a5a7ab1e66f195ac2c9c8de8a9e7f7f60a8
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

<<<<<<< HEAD
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
=======
>>>>>>> 77e04a5a7ab1e66f195ac2c9c8de8a9e7f7f60a8

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
    private UserRole userRole;
    @Builder
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
<<<<<<< HEAD

    @OneToMany(mappedBy = "user")
    Set<UserChatRoom> userChatRooms = new LinkedHashSet<>();
=======
    public void updateUser(String nickname, String password){
        this.nickname = nickname;
        this.password = password;
    }
>>>>>>> 77e04a5a7ab1e66f195ac2c9c8de8a9e7f7f60a8
}
