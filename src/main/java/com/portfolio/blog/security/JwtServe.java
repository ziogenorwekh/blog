package com.portfolio.blog.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.portfolio.blog.redis.RedisAuthenticationService;
import com.portfolio.blog.redis.RedisRepo;
import com.portfolio.blog.vo.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Deprecated this class.
 */
@Deprecated
@Component
@RequiredArgsConstructor
public class JwtServe {

    @Value("${jwt.secret.key}")
    private String secretKey;

    public Algorithm algorithm;


    private final RedisRepo redisRepo;


    @PostConstruct
    private void init() {
        this.algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public Map<String, String> createToken(Token token) {

        Map<String, String> respToken = new HashMap<>();

        respToken.put("access_token", createAccess(token).get("access_token"));

        return respToken;
    }

    // 첫번재 로그인 리프레시는 여기서 관리
    public Map<String, String> createAccess(Token token) {
        String access = JWT.create().withSubject(token.getMemberId())
                .withIssuer(token.getName())
                .withArrayClaim("roles", token.getRoles().toArray(new String[0]))
                .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // 15 minutes
                .sign(algorithm);
        Map<String, String> accessToken = new HashMap<>();
        accessToken.put("access_token", access);
        Map<String, String> refresh = this.createRefresh(token);
        redisRepo.saveRefresh(refresh.get("issuer"),refresh.get("refresh"));
        return accessToken;
    }

    public Map<String, String> createRefresh(Token token) {
        String refresh = JWT.create()
                .withSubject(token.getMemberId())
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .sign(algorithm);
        Map<String, String> refreshToken = new HashMap<>();
        refreshToken.put("refresh", refresh);
        refreshToken.put("issuer", token.getMemberId());
        return refreshToken;
    }

    public String getMemberIdByToken(String token) {
        return JWT.decode(token).getSubject();
    }
}
