package com.portfolio.blog.redis;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.portfolio.blog.domain.Member;
import com.portfolio.blog.domain.Role;
import com.portfolio.blog.exception.EmailAuthenticationException;
import com.portfolio.blog.exception.MemberNotFoundException;
import com.portfolio.blog.exception.RefreshTokenNotFoundException;
import com.portfolio.blog.repo.MemberRepository;
import com.portfolio.blog.vo.auth.Token;
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
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int random = (int) (Math.random() * 10);
            number.append(random);
        }
        redisRepo.save(number.toString(), email, 60 * 5L);
        this.sendMail(email, "?????? ?????? ?????? ??????", number.toString());
    }

    // ???????????? ???
    // ????????? ?????? ????????? ????????? ???????????? ??????,
//     ?????? ?????????????????? ?????????????????? ???????????? ??????.
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
     *
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
     *
     * @param token
     * @return
     */
    public Map<String, String> createAccess(Token token) {
        String access = JWT.create().withSubject(token.getMemberId())
                .withIssuer(token.getName())
                .withArrayClaim("roles", token.getRoles().toArray(new String[0]))
                .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // 15 min
                .sign(Algorithm.HMAC256(secretKey.getBytes()));
        Map<String, String> accessToken = new HashMap<>();
        accessToken.put("access_token", access);
        return accessToken;
    }

    // ????????? ????????? ????????? ?????? ?????? -> redisRepo ?????? ???????????? ????????? ?????? ???????????? ????????? ???????????? ????????? ????????? ?????????.
    @Transactional
    public Map<String, String> reCreateAccess(String memberId) {
        String refresh = redisRepo.findOne(memberId);
        if (refresh == null) {
            throw new RefreshTokenNotFoundException("refresh not in redis database");
        }
        String redisMemberId = JWT.decode(refresh).getSubject();
        Member member = memberRepository.findMemberByMemberId(redisMemberId)
                .orElseThrow(() -> new MemberNotFoundException("member not in database"));
        Token token = Token.builder()
                .name(member.getName())
                .memberId(member.getMemberId())
                .roles(member.getRoles().stream().map(Role::getRole).collect(Collectors.toList()))
                .build();
        return createAccess(token);
    }

    /**
     * refresh Token create -> redis in
     * @param token
     */
    @Transactional
    protected void createRefresh(Token token) {
        String isAlready = redisRepo.findOne(token.getMemberId());
        // ???????????? access ????????? ?????? ????????????????????? ??? ?????? ??????????????? redis ??? ?????? ?????? ????????? ??????
        if (isAlready != null) {
            redisRepo.delete(token.getMemberId());
        }
        String refresh = JWT.create()
                .withSubject(token.getMemberId())
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 1 minutes ? 24 Hours 24 * 60 * 60 * 1000
                .sign(Algorithm.HMAC256(secretKey.getBytes()));
        redisRepo.saveRefresh(token.getMemberId(), refresh);
    }

    @Transactional
    public void deleteRefresh(String memberId) {
        redisRepo.delete(memberId);
    }

    public String decodeTokenGetIssuer(String token) {
        String issuer = JWT.require(Algorithm.HMAC256(secretKey.getBytes())).build().verify(token)
                .getIssuer();
        if (issuer == null) {
            log.warn("issuer is null");
        }
        log.info(issuer);
        return issuer;
    }

}
