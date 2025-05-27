package com.resume.matcher.resumematchermain.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 경로에 대해 CORS 설정
                .allowedOrigins("http://localhost:3000")  // 허용할 출처(프론트엔드 주소)
                .allowedMethods("*")                      // 모든 HTTP 메서드 허용
                .allowedHeaders("*")                      // 모든 헤더 허용
                .allowCredentials(true);                  // 인증 정보 허용
    }
}
