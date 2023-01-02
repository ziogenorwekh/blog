package com.portfolio.blog.service;

import com.portfolio.blog.domain.Member;
import com.portfolio.blog.dto.MemberDto;
import com.portfolio.blog.exception.DuplicatedEmailException;
import com.portfolio.blog.exception.DuplicatedNameException;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String save(MemberCreate memberCreate) {
        validateDuplicatedEmail(memberCreate.getEmail());
        validateDuplicatedName(memberCreate.getName());
        memberCreate.setPassword(passwordEncoder.encode(memberCreate.getPassword()));
        Member member = Member.create(memberCreate);
        memberRepository.save(member);
        return member.getMemberId();
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
        validateDuplicatedName(memberUpdate.getName());
        memberUpdate.setPassword(passwordEncoder.encode(memberUpdate.getPassword()));
        member.update(memberUpdate);
        return new ModelMapper().map(member, MemberDto.class);
    }

    @Transactional
    public void delete(String memberId) {
        Member member = memberRepository.findMemberByMemberId(memberId).orElseThrow(() ->
                new MemberNotFoundException("member not in database")
        );
//        연관 객체 전체 제거
//        영속 전이와 고아 객체 제거로 타 Table 데이터 전체 제거
        member.delete();
        memberRepository.delete(member);
    }


    private void validateDuplicatedEmail(String email) {
        memberRepository.findMemberByEmail(email).ifPresent(member -> {
            throw new DuplicatedEmailException(String.format("%s is already exist", member.getEmail()));
        });
    }

    private void validateDuplicatedName(String name) {
        memberRepository.findMemberByName(name).ifPresent(member -> {
            throw new DuplicatedNameException(String.format("%s is already exist", member.getName()));
        });
    }

    @Transactional
    public void testVerified(String memberId) {
        Optional<Member> member = memberRepository.findMemberByMemberId(memberId);
        member.get().verified();
    }
}
