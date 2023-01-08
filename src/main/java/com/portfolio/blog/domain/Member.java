package com.portfolio.blog.domain;

import com.portfolio.blog.vo.WorkUrlCreate;
import com.portfolio.blog.vo.member.MemberCreate;
import com.portfolio.blog.vo.member.MemberUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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


    private String github;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Records> records = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<WorkUrl> workUrls = new ArrayList<>();


    @Builder
    public Member(String email, String name, String password, String realName,String github) {
        this.email = email;
        this.memberId = UUID.randomUUID().toString();
        this.name = name;
        this.realName = realName;
        this.password = password;
        this.github = github;
    }

    // Business Logic
    public static Member create(MemberCreate memberCreate) {
        Member member = Member.builder()
                .email(memberCreate.getEmail())
                .name(memberCreate.getName())
                .password(memberCreate.getPassword())
                .realName(memberCreate.getRealName())
                .github(memberCreate.getGithub())
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
        this.github = memberUpdate.getGithub();
        this.selfIntroduce = memberUpdate.getSelfIntroduce();
        this.name = memberUpdate.getName();
        this.password = memberUpdate.getPassword();
    }

    public void addWorkUrl(WorkUrlCreate workUrlCreate) {
        WorkUrl workUrl = WorkUrl.create(workUrlCreate, this);
        workUrl.addMember(this);
    }

    public void delete() {
        this.getRoles().clear();
        this.getPosts().clear();
        this.getRecords().clear();
        this.getWorkUrls().clear();
    }
}
