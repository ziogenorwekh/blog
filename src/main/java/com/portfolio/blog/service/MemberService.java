package com.portfolio.blog.service;

import com.portfolio.blog.domain.Member;
import com.portfolio.blog.dto.MemberDto;
import com.portfolio.blog.exception.DuplicatedEmailException;
import com.portfolio.blog.exception.DuplicatedNameException;
import com.portfolio.blog.exception.MemberNotFoundException;
import com.portfolio.blog.repo.MemberRepository;
import com.portfolio.blog.vo.member.MemberCreate;
import com.portfolio.blog.vo.member.MemberPwdUpdate;
import com.portfolio.blog.vo.member.MemberUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String save(MemberCreate memberCreate) {
        validateDuplicatedEmail(memberCreate.getEmail());
        validateDuplicatedName(memberCreate.getName());
        String rawPassword = memberCreate.getPassword();
        memberCreate.setPassword(passwordEncoder.encode(memberCreate.getPassword()));
        Member member = Member.create(memberCreate, rawPassword);
        memberRepository.save(member);
        return member.getMemberId();
    }

    @Transactional(readOnly = true)
    public MemberDto findOne(String memberId) {
        Member member = memberRepository.findMemberByMemberId(memberId).orElseThrow(() ->
                new MemberNotFoundException("member not in database"));
        MemberDto memberDto = new MemberDto(member);
        return memberDto;
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
        if (!member.getName().equals(memberUpdate.getName())) {
            validateDuplicatedName(memberUpdate.getName());
        }
        member.update(memberUpdate);
        return new ModelMapper().map(member, MemberDto.class);
    }

    @Transactional
    public MemberDto updatePwd(MemberPwdUpdate memberPwdUpdate, String memberId) {
        Member member = memberRepository.findMemberByMemberId(memberId).orElseThrow(() ->
                new MemberNotFoundException("member not in database")
        );
        memberPwdUpdate.setPassword(passwordEncoder.encode(memberPwdUpdate.getPassword()));
        member.updatePwd(memberPwdUpdate);
        return new ModelMapper().map(member, MemberDto.class);
    }

    @Transactional
    public void delete(String memberId) {
        Member member = memberRepository.findMemberByMemberId(memberId).orElseThrow(() ->
                new MemberNotFoundException("member not in database")
        );
//        ?????? ?????? ?????? ??????
//        ?????? ????????? ?????? ?????? ????????? ??? Table ????????? ?????? ??????
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
        member.get().setWorkUrl("http://127.0.0.1/work");
        member.get().setGithub("ziogenorwekh");
        member.get().setSelfIntroduce("<p><span style=\"color: rgb(255, 255, 102);" +
                "\">Hello! </span><span style=\"color: rgb(255, 255, 255);\">" +
                "Lorem ipsum dolor sit amet, consectetur adipisicing elit. " +
                "Dolore enim fugit minima minus nemo suscipit? </span>" +
                "<span style=\"color: rgb(230, 0, 0);\">A accusamus eos</span>" +
                "<span style=\"color: rgb(255, 255, 255);\"> est facilis magnam modi nihil similique, " +
                "tempore, ullam unde, veritatis voluptas? Quia!</span></p>");
        member.get().verified();
    }
}
