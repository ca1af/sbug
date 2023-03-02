package com.sparta.sbug.user.repository;

import com.sparta.sbug.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.sparta.sbug.cache.CacheNames;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 이메일로 유저 엔티티를 조회
     *
     * @param email 이메일
     * @return Optional&lt;User&gt;
     */
    @Cacheable(cacheNames = CacheNames.USERBYEMAIL, key = "#email")
    Optional<User> findByEmailAndInUseIsTrue(String email);

    /**
     * 이메일로 대상 유저를 삭제 ( 논리 삭제 )
     *
     * @param email 이메일
     */
    @Query("update User u set u.inUse = false where u.email = :email")
    @Modifying(clearAutomatically = true)
    void disableInUseByEmail(@Param("email") String email);

    Optional<User> findByKakaoIdAndInUseIsTrue(Long kakaoId);
}
