package com.sparta.sbug.security.userDetails;

import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.repository.UserRepository;
import com.sparta.sbug.cache.CacheNames;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

// lombok
@RequiredArgsConstructor

// springframework stereotype
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * 이메일로 사용자 객체를 찾아 UserDetails 구현체에 담습니다.
     *
     * @param email : 유저를 식별할 수 있는 이메일
     * @return UserDetails
     */
    @Override
    @Cacheable(cacheNames = CacheNames.USER, key = "#email")
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndInUseIsTrue(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new UserDetailsImpl(user, user.getEmail());
    }
}
