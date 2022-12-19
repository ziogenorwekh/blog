package com.portfolio.blog.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Login {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String name;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
