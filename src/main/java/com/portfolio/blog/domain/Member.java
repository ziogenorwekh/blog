package com.portfolio.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.portfolio.blog.exception.PasswordNotMatchedException;
import com.portfolio.blog.vo.member.MemberCreate;
import com.portfolio.blog.vo.member.MemberPwdUpdate;
import com.portfolio.blog.vo.member.MemberUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID", nullable = false)
    private Long id;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "MEMBER_UUID", unique = true)
    private String memberId;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String realName;

    @Lob
    private String selfIntroduce;

    @Setter
    private String github;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @JsonIgnore
    private String rawPassword;

    @Setter
    private String workUrl;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Records> records = new ArrayList<>();




    @Builder
    public Member(String email, String name, String password, String realName,String github,String rawPassword) {
        this.email = email;
        this.memberId = UUID.randomUUID().toString();
        this.name = name;
        this.realName = realName;
        this.password = password;
        this.github = github;
        this.rawPassword = rawPassword;
//        리액트 parser 에러나서 그냥 p태그 넣음
        this.selfIntroduce = "<p></p>";
    }

    // Business Logic
    public static Member create(MemberCreate memberCreate,String rawPassword) {
        Member member = Member.builder()
                .email(memberCreate.getEmail())
                .name(memberCreate.getName())
                .password(memberCreate.getPassword())
                .realName(memberCreate.getRealName())
                .rawPassword(rawPassword)
                .build();
        Role role = Role.builder()
                .role("ROLE_NO_AUTH").build();
        role.addMember(member);
        return member;
    }

    public void verified() {
        roles.stream().filter(role -> role.getRole().equals("ROLE_NO_AUTH"))
                .collect(Collectors.toList()).stream()
                .forEach(role -> this.roles.remove(role));
        Role role = Role.builder().role("ROLE_USER")
                .build();
        role.addMember(this);
    }

    public void update(MemberUpdate memberUpdate) {
        this.workUrl = memberUpdate.getUrl();
        this.github = memberUpdate.getGithub();
        this.selfIntroduce = memberUpdate.getSelfIntroduce();
        this.name = memberUpdate.getName();
    }

    public void updatePwd(MemberPwdUpdate memberPwdUpdate) {
        if (!this.getRawPassword().equals(memberPwdUpdate.getCurrentPassword())) {
            throw new PasswordNotMatchedException("current password is not matched.");
        }
        this.password = memberPwdUpdate.getPassword();
    }


    public void delete() {
        this.getRoles().clear();
        this.getPosts().clear();
        this.getRecords().clear();
    }
}
