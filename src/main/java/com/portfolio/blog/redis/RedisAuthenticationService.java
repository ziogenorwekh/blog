package com.portfolio.blog.redis;

import com.portfolio.blog.domain.Member;
import com.portfolio.blog.exception.EmailAuthenticationException;
import com.portfolio.blog.exception.MemberNotFoundException;
import com.portfolio.blog.repo.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisAuthenticationService {

    private final RedisRepo redisRepo;

    private final MemberRepository memberRepository;

    private final JavaMailSender javaMailSender;

    public void sendMail(String to, String sub, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(sub);
        message.setText(text);

        javaMailSender.send(message);
    }

    @Transactional(readOnly = true)
    public void sendAuthentication(String email) {
        log.debug("sendAuthenticationTest email :: {}", email);
        memberRepository.findMemberByEmail(email)
                .orElseThrow(()->new MemberNotFoundException("member not in database"));
        String number = "";
        for (int i = 0; i < 6; i++) {
            int random = (int) (Math.random() * 10);
            number += random;
        }
        redisRepo.save(number,email,60*5L);
        this.sendMail(email, "회원 가입 인증 번호", number);
    }

    @Transactional
    public void verifyAuthentication(String key) {
        String email = redisRepo.findOne(key);
        if (email == null) {
            throw new EmailAuthenticationException("expired");
        }
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(()->new MemberNotFoundException("member not in database"));
        member.verified();
        redisRepo.delete(key);
    }
}
