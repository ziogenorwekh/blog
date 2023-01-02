package com.portfolio.blog.security;

import com.portfolio.blog.domain.Member;
import com.portfolio.blog.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class CustomizedMemberDetails implements UserDetails {

    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        member.getRoles().forEach(role -> grantedAuthorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return role.getRole();
            }
        }));

        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

//    이메일 인증을 안받았을 경우 사용 불가
    @Override
    public boolean isEnabled() {
        Optional<Role> role_no_auth = member.getRoles().stream()
                .filter(role -> role.getRole().equals("ROLE_NO_AUTH")).findAny();
        return !role_no_auth.isPresent();
    }
}
