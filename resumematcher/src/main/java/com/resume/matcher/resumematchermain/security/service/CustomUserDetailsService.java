package com.resume.matcher.resumematchermain.security.service;

import com.resume.matcher.resumematchermain.user.domain.User;
import com.resume.matcher.resumematchermain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())  // 암호화된 비밀번호 저장되어 있어야 함
                .authorities("USER")  // 역할 넣기
                .build();
    }
}
