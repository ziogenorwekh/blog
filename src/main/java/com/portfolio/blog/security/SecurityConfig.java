package com.portfolio.blog.security;

import com.portfolio.blog.exceptionhandler.CustomizedAccessDeniedHandler;
import com.portfolio.blog.exceptionhandler.CustomizedAuthenticationEntryPoint;
import com.portfolio.blog.redis.RedisAuthenticationService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private AuthenticationConfiguration authenticationConfiguration;
    private RedisAuthenticationService redisAuthenticationService;
    private CustomizedMemberDetailsService detailsService;

    @Autowired
    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, RedisAuthenticationService redisAuthenticationService
            , CustomizedMemberDetailsService detailsService) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.redisAuthenticationService = redisAuthenticationService;
        this.detailsService = detailsService;
    }

    @Bean
    @SneakyThrows(Exception.class)
    public SecurityFilterChain filterChain(HttpSecurity http) {

        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), redisAuthenticationService);
        http.httpBasic().disable();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.formLogin().disable();
        http.authorizeRequests().antMatchers(HttpMethod.GET).permitAll();
//        http.authorizeRequests().antMatchers(HttpMethod.POST).hasRole("USER");
//        http.authorizeRequests().antMatchers(HttpMethod.DELETE).hasRole("USER");
//        http.authorizeRequests().antMatchers(HttpMethod.PUT).hasRole("USER");
        http.authorizeRequests().antMatchers("/**").permitAll();
        http.addFilter(jwtAuthenticationFilter)
                .addFilterBefore(new JwtAuthorizationFilter(authenticationManager(authenticationConfiguration),detailsService, redisAuthenticationService), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling().accessDeniedHandler(new CustomizedAccessDeniedHandler())
                .authenticationEntryPoint(new CustomizedAuthenticationEntryPoint());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
