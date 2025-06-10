package com.resume.matcher.resumematchermain.resume.service;

import com.resume.matcher.resumematchermain.resume.domain.Resume;
import com.resume.matcher.resumematchermain.resume.repository.ResumeRepository;
import com.resume.matcher.resumematchermain.user.domain.User;
import com.resume.matcher.resumematchermain.user.repository.UserRepository;
import com.resume.matcher.resumematchermain.resume.util.KeywordExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;
import java.util.Optional;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final KeywordExtractor keywordExtractor;
    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository, KeywordExtractor keywordExtractor) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.keywordExtractor = keywordExtractor;
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

    public Resume uploadAndParsePdf(MultipartFile file, Long userId) throws IOException {
        // 1. 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        // 2. PDF 텍스트 추출
        String text = extractTextFromPdf(file);

        List<String> keywords = keywordExtractor.extractKeywords(text);

        // 3. 이력서 저장 (title은 파일 이름 사용)
        Resume resume = new Resume();
        resume.setTitle(file.getOriginalFilename());
        resume.setContent(text);
        resume.setUser(user);
        resume.setKeywords(keywords);

        return resumeRepository.save(resume);
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }


}

