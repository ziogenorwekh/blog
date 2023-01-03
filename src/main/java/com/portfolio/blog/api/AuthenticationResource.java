package com.portfolio.blog.api;

import com.portfolio.blog.domain.Member;
import com.portfolio.blog.exception.MemberNotFoundException;
import com.portfolio.blog.redis.RedisAuthenticationService;
import com.portfolio.blog.vo.EmailVerify;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationResource {

    private final RedisAuthenticationService redisAuthenticationService;


    @ApiOperation(value = "회원 등록 이메일 전송", notes = "가입한 회원이 권한 부여받도록 메일을 전송")
    @RequestMapping(value = "/mails", method = RequestMethod.POST)
    public ResponseEntity<Void> sendAuthentication(@RequestBody @Validated EmailVerify requestVo) {
        log.debug(requestVo.getEmail());
        redisAuthenticationService.sendAuthentication(requestVo.getEmail());
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "인증 번호 확인", notes = "가입한 회원에게 권한 부여")
    @RequestMapping(value = "/mails/check", method = RequestMethod.POST)
    public ResponseEntity<Void> checkAuthentication(@RequestBody @Validated EmailVerify requestVo) {
        redisAuthenticationService.verifyAuthentication(requestVo.getNumber());
        return ResponseEntity.accepted().build();
    }


    @ApiOperation(value = "토큰 재발급", notes = "액세스 토큰을 재발급하고, 재발급 토큰을 redis 에 저장합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "재발급 성공"),
            @ApiResponse(code = 404, message = "해당 회원 존재하지 않음"),
    })
    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> refresh(@AuthenticationPrincipal @ApiIgnore Member member) {
        Map<String, String> token = redisAuthenticationService.createToken(member.getMemberId());
        return ResponseEntity.ok(token);
    }

    @ApiOperation(value = "토큰 삭제", notes = "재발급 토큰을 redis 에서 지웁니다.")
    @ApiResponse(code = 200, message = "삭제 성공")
    @RequestMapping(value = "/flush", method = RequestMethod.GET)
    public ResponseEntity<Void> logout(@AuthenticationPrincipal @ApiIgnore Member member) {
        if (member == null) {
            throw new MemberNotFoundException("already logout.");
        }
        redisAuthenticationService.deleteRefresh(member.getMemberId());
        return ResponseEntity.ok().build();
    }
}
