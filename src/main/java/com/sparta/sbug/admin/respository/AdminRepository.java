package com.sparta.sbug.admin.respository;

import com.sparta.sbug.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    /**
     * 이메일을 이용하여 관리자 데이터를 찾습니다.
     *
     * @param email 이메일
     * @return Optional&lt;Admin&gt;
     */
    Optional<Admin> findByEmail(String email);

    /**
     * 이메일을 이용하여 관리자 데이터가 존재하는지 검사합니다.
     *
     * @param email 이메일
     * @return Boolean
     */
    Boolean existsAdminByEmail(String email);

}