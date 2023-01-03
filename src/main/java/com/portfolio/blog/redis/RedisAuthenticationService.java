package com.portfolio.blog.redis;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.portfolio.blog.domain.Member;
import com.portfolio.blog.domain.Role;
import com.portfolio.blog.exception.EmailAuthenticationException;
import com.portfolio.blog.exception.MemberNotFoundException;
import com.portfolio.blog.repo.MemberRepository;
import com.portfolio.blog.vo.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisAuthenticationService {

    private final RedisRepo redisRepo;

    private final MemberRepository memberRepository;

    private final JavaMailSender javaMailSender;

    @Value("${jwt.secret.key}")
    private String secretKey;

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
                .orElseThrow(() -> new MemberNotFoundException("member not in database"));
        String number = "";
        for (int i = 0; i < 6; i++) {
            int random = (int) (Math.random() * 10);
            number += random;
        }
        redisRepo.save(number, email, 60 * 5L);
        this.sendMail(email, "회원 가입 인증 번호", number);
    }

    // 수정해야 됌
    // 잘못된 키면 잘못된 키라고 전달해야 하고,
//     키가 만료되었으면 만료되었다고 전달해야 한다.
    @Transactional
    public void verifyAuthentication(String key) {

        log.debug("verifyAuthentication :: {}", key);
        String email = redisRepo.findOne(key);
        if (email == null) {
            throw new EmailAuthenticationException("wrong or expired");
        }
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("member not in database"));
        member.verified();
        redisRepo.delete(key);
    }


    /**
     * first token create
     * @param memberId
     * @return
     */
    @Transactional
    public Map<String, String> createToken(String memberId) {
        Member member = memberRepository.findMemberByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("member not in database"));

        Token token = new Token(member.getMemberId(), member.getName(),
                member.getRoles().stream().map(Role::getRole).collect(Collectors.toList()));

        Map<String, String> respToken = new HashMap<>();

        respToken.put("access_token", createAccess(token).get("access_token"));
        this.createRefresh(token);
        return respToken;
    }

    /**
     * access Token create
     * @param token
     * @return
     */
    public Map<String, String> createAccess(Token token) {
        String access = JWT.create().withSubject(token.getMemberId())
                .withIssuer(token.getName())
                .withArrayClaim("roles", token.getRoles().toArray(new String[0]))
                .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // 15 minutes
                .sign(Algorithm.HMAC256(secretKey.getBytes()));
        Map<String, String> accessToken = new HashMap<>();
        accessToken.put("access_token", access);
        return accessToken;
    }

    /**
     * refresh Token create -> redis in
     * @param token
     */
    @Transactional
    protected void createRefresh(Token token) {
        String refresh = JWT.create()
                .withSubject(token.getMemberId())
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24 Hours
                .sign(Algorithm.HMAC256(secretKey.getBytes()));
        redisRepo.saveRefresh(token.getMemberId(), refresh);
    }

    public String decodeTokenGetIssuer(String token) {
        return JWT.decode(token).getIssuer();
    }
}
