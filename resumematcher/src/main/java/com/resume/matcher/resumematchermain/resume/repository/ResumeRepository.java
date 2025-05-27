package com.resume.matcher.resumematchermain.resume.repository;

import com.resume.matcher.resumematchermain.resume.domain.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    // 특정 유저의 이력서 목록 조회용 메서드 예시
    List<Resume> findByUserId(Long userId);
}
