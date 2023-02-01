package com.portfolio.blog.api;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.portfolio.blog.domain.Member;
import com.portfolio.blog.domain.activity.Activity;
import com.portfolio.blog.dto.ActivityDto;
import com.portfolio.blog.service.ActivityService;
import com.portfolio.blog.vo.activity.ActivityRequest;
import com.portfolio.blog.vo.activity.ActivityResponse;
import com.portfolio.blog.vo.member.MemberCreate;
import com.portfolio.blog.vo.member.MemberResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ActivityResource {

    private final ActivityService activityService;

    @ApiOperation(value = "활동 저장", notes = "지정된 폼 형식에 맞는 활정 정보 저장")
    @ApiResponses({
            @ApiResponse(code = 201, message = "성공"),
            @ApiResponse(code = 400, message = "지정된 폼 형식에 맞지 않음"),
            @ApiResponse(code = 401, message = "권한 없음")
    })
    @RequestMapping(value = "/activities", method = RequestMethod.POST)
    public ResponseEntity<URI> create(@RequestBody @Validated ActivityRequest activityRequest,
                                      @AuthenticationPrincipal Member member) {

        String id = activityService.save(activityRequest, member.getMemberId());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "활동 조회", notes = "특정 활동을 단건 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 404, message = "찾을 수 없음"),
    })
    @RequestMapping(value = "/activities/{activityId}", method = RequestMethod.GET)
    public ResponseEntity<MappingJacksonValue> retrieveActivities(@PathVariable String activityId) {
        ActivityDto activityDto = activityService.findOne(activityId);
        EntityModel<ActivityResponse> model = EntityModel.of(new ModelMapper().map(activityDto, ActivityResponse.class));
        model.add(linkTo(methodOn(ActivityResource.class).update(activityId, null, null)).withRel("update"));
        model.add(linkTo(methodOn(ActivityResource.class).delete(activityId, null)).withRel("delete"));

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("activityId", "name", "story", "startPeriod", "endPeriod", "activityType");

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("activity", filter);

        MappingJacksonValue jacksonValue = new MappingJacksonValue(model);
        jacksonValue.setFilters(filterProvider);
        return ResponseEntity.ok(jacksonValue);
    }

    @ApiOperation(value = "활동 전체 조회", notes = "특정 회원의 활동을 전체 조회합니다.")
    @RequestMapping(value = "{memberId}/activities", method = RequestMethod.GET)
    public ResponseEntity<MappingJacksonValue> retrieveAllActivities(
            @PathVariable String memberId) {
        List<ActivityDto> list = activityService.findAll(memberId);
        return getMappingJacksonValueResponseEntity(list);
    }

    @ApiOperation(value = "활동 전체 조회", notes = "해당 타입에 맞는 특정 회원의 활동을 전체 조회합니다.")
    @RequestMapping(value = "{memberId}/activities/{type}", method = RequestMethod.GET)
    public ResponseEntity<MappingJacksonValue> retrieveAllActivitiesByType(
            @PathVariable String type,
            @PathVariable String memberId) {
        List<ActivityDto> list = activityService.findAllByType(type, memberId);
        return getMappingJacksonValueResponseEntity(list);
    }



    @ApiOperation(value = "활동 업데이트", notes = "특정 활동을 단건 업데이트합니다.")
    @ApiResponses({
            @ApiResponse(code = 202, message = "수정 성공"),
            @ApiResponse(code = 400, message = "잘못된 폼 형식"),
            @ApiResponse(code = 401, message = "권한 없음"),
            @ApiResponse(code = 404, message = "존재하지 않음"),
    })
    @RequestMapping(value = "/activities/{activityId}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable String activityId,
                                    @AuthenticationPrincipal Member member,
                                    @RequestBody @Validated ActivityRequest activityRequest) {
        ActivityDto activityDto = activityService.update(member.getMemberId(), activityId, activityRequest);
        ActivityResponse activityResponse = new ModelMapper().map(activityDto, ActivityResponse.class);
        EntityModel<ActivityResponse> model = EntityModel.of(activityResponse);
        model.add(linkTo(methodOn(ActivityResource.class).retrieveActivities(activityId)).withRel("find-all-activities"));
        model.add(linkTo(methodOn(ActivityResource.class).delete(activityId, member)).withRel("delete"));
        model.add(linkTo(methodOn(ActivityResource.class).retrieveActivities(activityId)).withRel("find-this"));

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("name", "story", "startPeriod", "endPeriod", "activityType");

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("activity", filter);

        MappingJacksonValue jacksonValue = new MappingJacksonValue(model);
        jacksonValue.setFilters(filterProvider);
        return ResponseEntity.accepted().body(jacksonValue);
    }

    @ApiOperation(value = "활동 삭제", notes = "특정 활동을 단건 삭제합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "삭제 성공"),
            @ApiResponse(code = 401, message = "권한 없음"),
            @ApiResponse(code = 404, message = "존재하지 않음"),
    })
    @RequestMapping(value = "/activities/{activityId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String activityId, @AuthenticationPrincipal Member member) {
        activityService.delete(member.getMemberId(), activityId);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<MappingJacksonValue> getMappingJacksonValueResponseEntity(List<ActivityDto> list) {
        List<ActivityResponse> responses = list.stream()
                .map(activityDto -> new ModelMapper().map(activityDto, ActivityResponse.class)).collect(Collectors.toList());
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("activityId", "name", "story", "startPeriod", "endPeriod", "activityType");

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("activity", filter);

        MappingJacksonValue jacksonValue = new MappingJacksonValue(responses);
        jacksonValue.setFilters(filterProvider);
        return ResponseEntity.ok(jacksonValue);
    }
}
