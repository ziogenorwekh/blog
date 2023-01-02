package com.portfolio.blog.domain;

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

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "MEMBER_UUID", unique = true)
    private String memberId;

    @Column(unique = true)
    private String name;

    private String password;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @Builder
    public Member(String email, String name, String password) {
        this.email = email;
        this.memberId = UUID.randomUUID().toString();
        this.name = name;
        this.password = password;
    }

    // Business Logic
    public static Member create(MemberCreate memberCreate) {
        Member member = Member.builder()
                .email(memberCreate.getEmail())
                .name(memberCreate.getName())
                .password(memberCreate.getPassword())
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
        this.name = memberUpdate.getName();
        this.password = memberUpdate.getPassword();
    }

    public void delete() {
        this.getRoles().clear();
        this.getPosts().clear();
    }
}
