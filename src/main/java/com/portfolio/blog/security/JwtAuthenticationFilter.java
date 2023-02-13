package com.portfolio.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.blog.redis.RedisAuthenticationService;
import com.portfolio.blog.vo.auth.Login;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationManager authenticationManager;
    private final RedisAuthenticationService redisAuthenticationService;

    @Override
    @SneakyThrows(IOException.class)
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        Login login = new ObjectMapper().readValue(request.getInputStream(), Login.class);

        UsernamePasswordAuthenticationToken authenticationToken = null;

        try {
            authenticationToken = new
                    UsernamePasswordAuthenticationToken(login.getName(), login.getPassword());
        } catch (Exception e) {
            log.error("error");

            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.addHeader("Error",e.getMessage());
        }
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        CustomizedMemberDetails memberDetails = (CustomizedMemberDetails) authResult.getPrincipal();
        String memberId = memberDetails.getMemberId();
        String name = memberDetails.getUsername();
        Map<String, String> jwtServeToken = redisAuthenticationService.createToken(memberId);
        jwtServeToken.put("memberId", memberId);
        jwtServeToken.put("name", name);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), jwtServeToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.addHeader("Error", "Invalidated id or password");
    }
}
