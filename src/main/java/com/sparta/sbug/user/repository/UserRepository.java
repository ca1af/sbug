package com.sparta.sbug.user.repository;

import com.sparta.sbug.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 이메일로 유저 엔티티를 조회
     *
     * @param email 이메일
     * @return Optional&lt;User&gt;
     */
    Optional<User> findByEmail(String email);

    /**
     * 이메일로 대상 유저를 삭제
     *
     * @param email 이메일
     */
    void deleteByEmail(String email);
    Optional<User> findByKakaoId(Long kakaoId);
}
