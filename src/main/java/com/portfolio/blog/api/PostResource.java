package com.portfolio.blog.api;

import com.portfolio.blog.domain.Member;
import com.portfolio.blog.service.PostService;
import com.portfolio.blog.vo.PostCreate;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostResource {

    private final PostService postService;

    @ApiOperation(value = "글 작성",notes = "글 작성 폼에 맞는 글 생성")
    @ApiResponses({
            @ApiResponse(code = 201,message = "글 작성 성공"),
            @ApiResponse(code = 400,message = "폼 형식에 맞지 않음")
    })
    @RequestMapping(value = "/posts", method = RequestMethod.POST)
    public ResponseEntity<URI> create(PostCreate postCreate, @AuthenticationPrincipal Member member) {

        Long id = postService.save(postCreate, member.getMemberId());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).build();
    }


}
