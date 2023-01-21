package com.portfolio.blog.api;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.portfolio.blog.domain.Member;
import com.portfolio.blog.dto.MemberDto;
import com.portfolio.blog.exception.MemberNotFoundException;
import com.portfolio.blog.exception.UnAuthenticationAccessException;
import com.portfolio.blog.redis.RedisAuthenticationService;
import com.portfolio.blog.service.MemberService;
import com.portfolio.blog.vo.EmailVerify;
import com.portfolio.blog.vo.member.MemberCreate;
import com.portfolio.blog.vo.member.MemberPwdUpdate;
import com.portfolio.blog.vo.member.MemberResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationResource {

    private final RedisAuthenticationService redisAuthenticationService;
//    임시 DI
    private final MemberService memberService;


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


    @ApiOperation(value = "토큰 재발급", notes = "액세스 토큰을 재발급")
    @ApiResponses({
            @ApiResponse(code = 200, message = "재발급 성공"),
            @ApiResponse(code = 404, message = "해당 회원 존재하지 않음"),
    })
    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> refresh(@AuthenticationPrincipal @ApiIgnore Member member) {
        // 여기 수정해야 됌
        Map<String, String> token = redisAuthenticationService.reCreateAccess(member.getMemberId());
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

//    @ApiOperation(value = "회원가입", notes = "지정된 폼 형식에 맞는 회원 가입")
//    @ApiResponses({
//            @ApiResponse(code = 201, message = "회원가입 성공"),
//            @ApiResponse(code = 400, message = "지정된 폼 형식에 맞지 않음")
//    })
//    @RequestMapping(value = "/members", method = RequestMethod.POST)
//    public ResponseEntity<URI> createMember(@RequestBody @Validated MemberCreate memberCreate) {
//
////        Long id = method.save(null);
//        memberService.save(memberCreate);
////        redisAuthenticationService.
//        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
//        return ResponseEntity.created(uri).build();
//    }


    // 후에는 메일 인증으로 비밀번호 변경 예정
    @ApiOperation(value = "회원 비밀번호 수정", notes = "회원 비밀번호를 수정합니다.")
    @ApiResponses({
            @ApiResponse(code = 202, message = "수정 성공"),
            @ApiResponse(code = 400, message = "조건에 맞지 않는 폼 형식"),
            @ApiResponse(code = 401, message = "권한 없는 접근"),
            @ApiResponse(code = 404, message = "존재하지 않음")
    })
    @ApiImplicitParam(name = "memberId", value = "회원 UUID",dataTypeClass = String.class)
    @RequestMapping(value = "/members/{memberId}", method = RequestMethod.PUT)
    public ResponseEntity<MappingJacksonValue> updateMemberPwd(@PathVariable String memberId,
                                                               @AuthenticationPrincipal @ApiIgnore Member member,
                                                               @RequestBody @Validated MemberPwdUpdate memberPwdUpdate) {
        if (member == null || !member.getMemberId().equals(memberId)) {
            throw new UnAuthenticationAccessException("You do not have permission.");
        }

        MemberDto memberDto = memberService.updatePwd(memberPwdUpdate, member.getMemberId());

        MemberResponse memberResponse = new MemberResponse(memberDto);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("memberId", "name", "email", "realName", "selfIntroduce", "workUrl", "github");

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("memberResp", filter);

        MappingJacksonValue jacksonValue = new MappingJacksonValue(memberResponse);
        jacksonValue.setFilters(filterProvider);
        return ResponseEntity.accepted().body(jacksonValue);
    }
}
