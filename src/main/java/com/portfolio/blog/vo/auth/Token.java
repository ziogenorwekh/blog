package com.portfolio.blog.vo.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class Token {

    private String memberId;
    private String name;
    private List<String> roles;


    @Builder
    public Token(String memberId, String name, List<String> roles) {
        this.memberId = memberId;
        this.name = name;
        this.roles = roles;
    }
}
