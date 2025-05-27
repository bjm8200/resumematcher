package com.resume.matcher.resumematchermain.user.service;

import com.resume.matcher.resumematchermain.user.domain.User;
import com.resume.matcher.resumematchermain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(String email, String password, String name) {
        // 중복 이메일 체크
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        });

        // 비밀번호 암호화
        String hashedPassword = passwordEncoder.encode(password);

        User newUser = new User(email, hashedPassword, name);
        return userRepository.save(newUser);
    }

    @Transactional(readOnly = true)
    public User login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));
    }
}
