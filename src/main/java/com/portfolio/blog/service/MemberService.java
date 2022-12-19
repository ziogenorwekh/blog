package com.portfolio.blog.service;

import com.portfolio.blog.domain.Member;
import com.portfolio.blog.dto.MemberDto;
import com.portfolio.blog.exception.DuplicatedEmailException;
import com.portfolio.blog.exception.MemberNotFoundException;
import com.portfolio.blog.repo.MemberRepository;
import com.portfolio.blog.vo.member.MemberCreate;
import com.portfolio.blog.vo.member.MemberUpdate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long save(MemberCreate memberCreate) {
        validateDuplicatedEmail(memberCreate.getEmail());
        memberCreate.setPassword(passwordEncoder.encode(memberCreate.getPassword()));
        Member member = Member.create(memberCreate);
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional(readOnly = true)
    public MemberDto findOne(String memberId) {
        Member member = memberRepository.findMemberByMemberId(memberId).orElseThrow(() ->
                new MemberNotFoundException("member not in database"));
        return new ModelMapper().map(member, MemberDto.class);
    }

    @Transactional(readOnly = true)
    public List<MemberDto> findAll() {
        List<Member> members = memberRepository.findAll();
        return members.stream().map(member -> new ModelMapper().map(member, MemberDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public MemberDto update(MemberUpdate memberUpdate, String memberId) {
        Member member = memberRepository.findMemberByMemberId(memberId).orElseThrow(() ->
                new MemberNotFoundException("member not in database")
        );
        memberUpdate.setPassword(passwordEncoder.encode(memberUpdate.getPassword()));
        member.update(memberUpdate);
        return new ModelMapper().map(member, MemberDto.class);
    }

    @Transactional
    public void delete(String memberId) {
        Member member = memberRepository.findMemberByMemberId(memberId).orElseThrow(() ->
                new MemberNotFoundException("member not in database")
        );
        memberRepository.delete(member);
    }


    private void validateDuplicatedEmail(String email) {
        memberRepository.findMemberByEmail(email).ifPresent(member -> {
            throw new DuplicatedEmailException("email duplicated");
        });
    }
}
