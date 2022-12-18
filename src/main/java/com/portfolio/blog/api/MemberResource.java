package com.portfolio.blog.api;

import com.portfolio.blog.domain.Member;
import com.portfolio.blog.dto.MemberDto;
import com.portfolio.blog.exception.UnAuthenticationAccessException;
import com.portfolio.blog.service.MemberService;
import com.portfolio.blog.vo.MemberCreate;
import com.portfolio.blog.vo.MemberResponse;
import com.portfolio.blog.vo.MemberUpdate;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberResource {


    private final MemberService memberService;

    @ApiOperation(value = "회원가입", notes = "지정된 폼 형식에 맞는 회원 가입")
    @ApiResponses({
            @ApiResponse(code = 201, message = "회원가입 성공"),
            @ApiResponse(code = 400, message = "지정된 폼 형식에 맞지 않음")
    })
    @RequestMapping(value = "/members", method = RequestMethod.POST)
    public ResponseEntity<URI> create(@RequestBody @Validated MemberCreate memberCreate) {
        Long id = memberService.save(memberCreate);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "회원 조회", notes = "회원 아이디를 통해 회원 검색")
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원 존재"),
            @ApiResponse(code = 404, message = "회원 존재하지 않음")
    })
    @RequestMapping(value = "/members/{memberId}", method = RequestMethod.GET)
    public ResponseEntity<EntityModel> retrieveMember(@PathVariable String memberId) {
        MemberDto memberDto = memberService.findOne(memberId);
        EntityModel<MemberDto> model = EntityModel.of(memberDto);
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(MemberResource.class).update(null, memberId, null));
        model.add(linkBuilder.withRel("delete-member"));
        model.add(linkBuilder.withRel("update-member"));

        return ResponseEntity.ok(model);
    }

    @ApiOperation(value = "회원 수정", notes = "회원 아이디를 통해 회원 수정")
    @ApiResponses({
            @ApiResponse(code = 202, message = "수정 성공"),
            @ApiResponse(code = 400, message = "조건에 맞지 않는 폼 형식"),
            @ApiResponse(code = 401, message = "권한 없는 접근"),
            @ApiResponse(code = 404, message = "존재하지 않음")
    })
    @RequestMapping(value = "/members/{memberId}",method = RequestMethod.PUT)
    public ResponseEntity<EntityModel<MemberDto>> update(@RequestBody @Validated MemberUpdate memberUpdate
    , @PathVariable String memberId, @AuthenticationPrincipal @ApiIgnore Member member) {
        this.checkAuthentication(member,memberId);
        MemberDto memberDto = memberService.update(memberUpdate, memberId);

        EntityModel<MemberDto> model = EntityModel.of(memberDto);
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(MemberResource.class).memberService.findOne(memberId));
        model.add(linkBuilder.withRel("delete-member"));
        model.add(linkBuilder.withRel("search-member"));

        return ResponseEntity.accepted().body(model);
    }

    @ApiOperation(value = "회원 삭제", notes = "회원 아이디를 통해 회원 삭제")
    @ApiResponses({
            @ApiResponse(code = 200, message = "삭제 성공"),
            @ApiResponse(code = 401, message = "권한 없는 접근"),
            @ApiResponse(code = 404, message = "존재하지 않음")
    })
    @RequestMapping(value = "/members/{memberId}", method = RequestMethod.GET)
    public ResponseEntity<Void> delete(@PathVariable String memberId, @AuthenticationPrincipal @ApiIgnore Member member) {
        this.checkAuthentication(member,memberId);
        memberService.delete(memberId);
        return ResponseEntity.ok().build();
    }


    private void checkAuthentication(Member member, String memberId) {
        if (!member.getMemberId().equals(memberId)) {
            throw new UnAuthenticationAccessException("you're not access this entity");
        }
    }

}
