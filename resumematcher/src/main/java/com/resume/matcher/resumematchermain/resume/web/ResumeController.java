package com.resume.matcher.resumematchermain.resume.web;

import com.resume.matcher.resumematchermain.resume.domain.Resume;
import com.resume.matcher.resumematchermain.resume.service.ResumeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/resumes")
@Slf4j
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    // --- Create DTO ---
    @Getter @Setter
    public static class CreateResumeRequest {
        @NotNull(message = "사용자 ID는 필수입니다.")
        private Long userId;

        @NotBlank(message = "제목은 필수입니다.")
        private String title;

        @NotBlank(message = "내용은 필수입니다.")
        private String content;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createResume(@Valid @RequestBody CreateResumeRequest request) {
        try {
            log.info("Resume 생성 요청: userId={}, title={}", request.getUserId(), request.getTitle());
            Resume resume = resumeService.createResume(request.getUserId(), request.getTitle(), request.getContent());
            return ResponseEntity.ok(resume);
        } catch (IllegalArgumentException e) {
            log.warn("이력서 생성 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getResume(@PathVariable("id") Long id) {
        Optional<Resume> resume = resumeService.getResume(id);
        return resume.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("이력서 조회 실패: id={} 없음", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Resume>> getResumesByUser(@PathVariable("userId") Long userId) {
        List<Resume> resumes = resumeService.getResumesByUser(userId);
        log.info("사용자 {}의 이력서 {}건 조회됨", userId, resumes.size());
        return ResponseEntity.ok(resumes);
    }

    // --- Update DTO ---
    @Getter @Setter
    public static class UpdateResumeRequest {
        @NotBlank(message = "제목은 필수입니다.")
        private String title;

        @NotBlank(message = "내용은 필수입니다.")
        private String content;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateResume(@PathVariable("id") Long id, @Valid @RequestBody UpdateResumeRequest request) {
        try {
            log.info("Resume 수정 요청: id={}, title={}", id, request.getTitle());
            Resume updated = resumeService.updateResume(id, request.getTitle(), request.getContent());
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            log.warn("이력서 수정 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResume(@PathVariable("id") Long id) {
        try {
            log.info("Resume 삭제 요청: id={}", id);
            resumeService.deleteResume(id);
            return ResponseEntity.ok("삭제 성공");
        } catch (Exception e) {
            log.error("이력서 삭제 실패: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("삭제 실패: " + e.getMessage());
        }
    }
}
