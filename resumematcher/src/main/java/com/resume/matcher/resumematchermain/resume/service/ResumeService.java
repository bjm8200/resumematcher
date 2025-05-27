package com.resume.matcher.resumematchermain.resume.service;

import com.resume.matcher.resumematchermain.resume.domain.Resume;
import com.resume.matcher.resumematchermain.resume.repository.ResumeRepository;
import com.resume.matcher.resumematchermain.user.domain.User;
import com.resume.matcher.resumematchermain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }

    // 이력서 등록
    @Transactional
    public Resume createResume(Long userId, String title, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        Resume resume = new Resume(title, content, user);
        return resumeRepository.save(resume);
    }

    // 특정 이력서 조회
    public Optional<Resume> getResume(Long resumeId) {
        return resumeRepository.findById(resumeId);
    }

    // 특정 사용자의 모든 이력서 조회
    public List<Resume> getResumesByUser(Long userId) {
        return resumeRepository.findByUserId(userId);
    }

    // 이력서 수정
    @Transactional
    public Resume updateResume(Long resumeId, String title, String content) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("이력서를 찾을 수 없습니다."));
        // setter가 없다면 필드를 바꾸는 메서드를 만들거나 리플렉션 없이 필드 수정 구현 필요
        resume.setTitle(title);
        resume.setContent(content);
        return resume;
    }

    // 이력서 삭제
    @Transactional
    public void deleteResume(Long resumeId) {
        resumeRepository.deleteById(resumeId);
    }
}
