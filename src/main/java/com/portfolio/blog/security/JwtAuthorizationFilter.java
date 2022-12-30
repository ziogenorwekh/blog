package com.portfolio.blog.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private CustomizedMemberDetailsService memberDetailsService;
    private JwtServe jwtServe;

    @Autowired
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,CustomizedMemberDetailsService memberDetailsService,JwtServe jwtServe) {
        super(authenticationManager);
        this.memberDetailsService = memberDetailsService;
        this.jwtServe = jwtServe;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        log.debug("doFilterInternal");
        String header = request.getHeader(AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ") || request.getServletPath().equals("/login")
                || request.getServletPath().equals("/api/refresh")) {
            chain.doFilter(request, response);
            return;
        }

        Authentication authentication = this.getAuthentication(request, response);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }


    private Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        log.debug("getAuthentication");
        String jwtHeader = request.getHeader(AUTHORIZATION).substring("Bearer ".length());
        CustomizedMemberDetails memberDetails;
        UsernamePasswordAuthenticationToken authenticationToken = null;
        try {
            memberDetails = (CustomizedMemberDetails) memberDetailsService.loadUserByUsername(
                    JWT.require(jwtServe.algorithm).build().verify(jwtHeader).getIssuer());
            authenticationToken =
                    new UsernamePasswordAuthenticationToken(memberDetails.getMember(),
                            null, memberDetails.getAuthorities());
        } catch (JWTDecodeException e) {
            exceptionHandler(response, e, HttpServletResponse.SC_BAD_REQUEST);
        } catch (TokenExpiredException e) {
            exceptionHandler(response, e, HttpServletResponse.SC_GONE);
        } catch (Exception e) {
            exceptionHandler(response, e, HttpServletResponse.SC_UNAUTHORIZED);
        }
        return authenticationToken;
    }

    private void exceptionHandler(HttpServletResponse response, Exception e, int status) {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(status);
        response.addHeader("Error", e.getMessage());
    }
}
