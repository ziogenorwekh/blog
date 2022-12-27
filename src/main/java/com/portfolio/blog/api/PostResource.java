package com.portfolio.blog.api;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.portfolio.blog.domain.Member;
import com.portfolio.blog.domain.PostSearch;
import com.portfolio.blog.dto.PostDto;
import com.portfolio.blog.service.PostService;
import com.portfolio.blog.vo.post.PostCreate;
import com.portfolio.blog.vo.post.PostResponse;
import com.portfolio.blog.vo.post.PostUpdate;
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
import java.util.List;

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
    @ApiResponses({
            @ApiResponse(code = 200, message = "조회 성공"),
            @ApiResponse(code = 404, message = "찾을 수 없음"),
    })
    @RequestMapping(value = "/posts/{postId}", method = RequestMethod.GET)
    public ResponseEntity<MappingJacksonValue> retrievePost(@PathVariable String postId) {

        PostDto postDto = postService.findOne(postId);
        PostResponse postResponse = new PostResponse(postDto);
        EntityModel<PostResponse> model = EntityModel.of(postResponse);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).update(null, postId, null));
        model.add(linkTo.withRel("update-post"));
        model.add(linkTo.withRel("delete-post"));
        MappingJacksonValue jacksonValue = getMappingJacksonValue(postResponse);
        return ResponseEntity.ok().body(jacksonValue);
    }


    @ApiOperation(value = "글 전체 조회", notes = "글 전체 조회")
    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public ResponseEntity<MappingJacksonValue> retrieveAllPosts() {
        List<PostDto> list = postService.findAll();
        List<PostResponse> collect = list.stream().map(PostResponse::new).toList();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("contents", "subTitle", "title");

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("postResp", filter);

        MappingJacksonValue jacksonValue = new MappingJacksonValue(collect);
        jacksonValue.setFilters(filterProvider);
        return ResponseEntity.ok(jacksonValue);
    }

    @ApiOperation(value = "글 수정", notes = "폼 형식에 맞는 글 수정")
    @ApiResponses({
            @ApiResponse(code = 202, message = "수정 성공"),
            @ApiResponse(code = 400, message = "폼에 맞지 않는 형식"),
            @ApiResponse(code = 401, message = "접근 권한 없음")
    })
    @RequestMapping(value = "/posts/{postId}", method = RequestMethod.PUT)
    public ResponseEntity<MappingJacksonValue> update(@RequestBody @Validated PostUpdate postUpdate,
                                                      @PathVariable String postId,
                                                      @AuthenticationPrincipal @ApiIgnore Member member) {
        PostSearch postSearch = new PostSearch(member.getMemberId(), postId);
        PostDto postDto = postService.update(postUpdate, postSearch);
        PostResponse postResponse = new PostResponse(postDto);
        EntityModel<PostResponse> model = EntityModel.of(postResponse);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrievePost(postId));
        model.add(linkTo.withRel("search-post"));
        model.add(linkTo.withRel("delete-post"));
        MappingJacksonValue jacksonValue = getMappingJacksonValue(postResponse);

        return ResponseEntity.accepted().body(jacksonValue);
    }

    @ApiOperation(value = "글 삭제", notes = "글 삭제")
    @ApiResponses({
            @ApiResponse(code = 202, message = "삭제 성공"),
            @ApiResponse(code = 401, message = "접근 권한 없음")
    })
    @RequestMapping(value = "/posts/{postId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String postId, @AuthenticationPrincipal @ApiIgnore Member member) {
        PostSearch postSearch = new PostSearch(member.getMemberId(), postId);
        postService.delete(postSearch);
        return ResponseEntity.ok().build();
    }

    private MappingJacksonValue getMappingJacksonValue(PostResponse postResponse) {

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("postId", "contents", "subTitle", "title", "postedBy", "postMember");

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("postResp", filter);

        MappingJacksonValue jacksonValue = new MappingJacksonValue(postResponse);
        jacksonValue.setFilters(filterProvider);
        return jacksonValue;
    }

}
