package com.portfolio.blog.api;

import com.portfolio.blog.redis.RedisAuthenticationService;
import com.portfolio.blog.vo.EmailVerify;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MailAuthenticationResource {

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

}
