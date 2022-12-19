package com.portfolio.blog.api;

import com.portfolio.blog.domain.Member;
import com.portfolio.blog.domain.PostSearch;
import com.portfolio.blog.dto.PostDto;
import com.portfolio.blog.service.PostService;
import com.portfolio.blog.vo.post.PostCreate;
import com.portfolio.blog.vo.post.PostUpdate;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostResource {

    private final PostService postService;

    @ApiOperation(value = "글 작성", notes = "글 작성 폼에 맞는 글 생성")
    @ApiResponses({
            @ApiResponse(code = 201, message = "글 작성 성공"),
            @ApiResponse(code = 400, message = "폼 형식에 맞지 않음")
    })
    @RequestMapping(value = "/posts", method = RequestMethod.POST)
    public ResponseEntity<URI> create(PostCreate postCreate, @AuthenticationPrincipal Member member) {

        Long id = postService.save(postCreate, member.getMemberId());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "글 조회", notes = "UUID 글 조회")
    @RequestMapping(value = "/posts/{postId}", method = RequestMethod.GET)
    public ResponseEntity<EntityModel> retrievePost(@PathVariable String postId) {

        PostDto postDto = postService.findOne(postId);
        EntityModel model = EntityModel.of(null);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()). null (null));
        model.add(linkTo.withRel("update-post"));
        return ResponseEntity.accepted().body(model);
    }

    @ApiOperation(value = "글 수정", notes = "폼 형식에 맞는 글 수정")
    @RequestMapping(value = "/posts/{postId}", method = RequestMethod.PUT)
    public ResponseEntity<EntityModel> update(@RequestBody @Validated PostUpdate postUpdate,
                                              @PathVariable String postId,
                                              @AuthenticationPrincipal @ApiIgnore Member member) {
        PostSearch postSearch = new PostSearch(member.getMemberId(), postId);
        PostDto postDto = postService.update(postUpdate, postSearch);
        EntityModel model = EntityModel.of(postDto);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrievePost(postId));
        model.add(linkTo.withRel("search-post"));
        return ResponseEntity.accepted().body(model);
    }


}
