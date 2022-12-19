package com.portfolio.blog.security;

import com.portfolio.blog.domain.Member;
import com.portfolio.blog.exception.MemberNotFoundException;
import com.portfolio.blog.repo.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomizedMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findMemberByName(username)
                .orElseThrow(() -> new MemberNotFoundException("member not in database"));

        UserDetails userDetails = new CustomizedMemberDetails(member);
        return userDetails;
    }
}
