package com.resume.matcher.resumematchermain.common.dto;

public class RegisterRequest {
    public String email;
    public String password;
    public String name;

    // 기본 생성자 (JSON 역직렬화용)
    public RegisterRequest() {}

    // 생성자 (테스트 등 필요시)
    public RegisterRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
