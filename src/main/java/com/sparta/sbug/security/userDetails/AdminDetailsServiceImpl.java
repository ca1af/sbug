package com.sparta.sbug.security.userDetails;

import com.sparta.sbug.admin.entity.Admin;
import com.sparta.sbug.admin.respository.AdminRepository;
import com.sparta.sbug.cache.CacheNames;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminDetailsServiceImpl implements UserDetailsService {
    private final AdminRepository adminRepository;

    /**
     * 이메일로 사용자 객체를 찾아 UserDetails 구현체에 담습니다.
     *
     * @param email : 유저를 식별할 수 있는 이메일
     * @return UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new AdminDetailsImpl(admin, admin.getEmail());
    }
}
