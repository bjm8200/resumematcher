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

    // 1. 이력서 등록
    @Transactional
    public Resume createResume(Long userId, String title, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
        Resume resume = new Resume(title, content, user);
        return resumeRepository.save(resume);
    }

    // 2. 특정 이력서 조회
    @Transactional(readOnly = true)
    public Optional<Resume> getResume(Long resumeId) {
        return resumeRepository.findById(resumeId);
    }

    // 3. 특정 사용자의 모든 이력서 조회
    @Transactional(readOnly = true)
    public List<Resume> getResumesByUser(Long userId) {
        return resumeRepository.findByUserId(userId);
    }

    // 4. 이력서 수정
    @Transactional
    public Resume updateResume(Long resumeId, String title, String content) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("이력서를 찾을 수 없습니다."));
        resume.setTitle(title);
        resume.setContent(content);
        return resume;
    }

    // 5. 이력서 삭제
    @Transactional
    public void deleteResume(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("이력서를 찾을 수 없습니다."));
        resumeRepository.delete(resume);
    }
}

