package com.portfolio.blog.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.portfolio.blog.vo.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtServe {

    @Value("${jwt.secret.key}")
    private String secretKey;

    public Algorithm algorithm;


    @PostConstruct
    private void init() {
        this.algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public Map<String, String> createToken(Token token) {

        Map<String, String> respToken = new HashMap<>();

        respToken.put("access_token", createAccess(token).get("access_token"));
        respToken.put("refresh_token", createRefresh(token).get("refresh_token"));
        return respToken;
    }

    public Map<String, String> createAccess(Token token) {
        String access = JWT.create().withSubject(token.getMemberId())
                .withIssuer(token.getName())
                .withArrayClaim("roles", token.getRoles().toArray(new String[0]))
                .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // 15 minutes
                .sign(algorithm);
        Map<String, String> accessToken = new HashMap<>();
        accessToken.put("access_token", access);
        return accessToken;
    }

    public Map<String, String> createRefresh(Token token) {
        String refresh = JWT.create()
                .withSubject(token.getMemberId())
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .sign(algorithm);
        Map<String, String> refreshToken = new HashMap<>();
        refreshToken.put("refresh_token", refresh);
        return refreshToken;
    }

    public String getMemberIdByToken(String token) {
        return JWT.decode(token).getSubject();
    }
}
