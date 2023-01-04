package com.portfolio.blog.api;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.portfolio.blog.domain.Member;
import com.portfolio.blog.dto.RecordsDto;
import com.portfolio.blog.service.RecordsService;
import com.portfolio.blog.vo.records.RecordCreate;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RecordsResource {
    private final RecordsService recordsService;

    @ApiOperation(value = "수상 또는 자격증 저장", notes = "수상 내역이나 자격증을 저장합니다. " +
            "단, 저장시에는 먼저 파일이 저장되어야 합니다.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "성공"),
            @ApiResponse(code = 400, message = "폼 형식에 맞지 않음"),
            @ApiResponse(code = 401, message = "해당 권한 없음"),
    })
    @RequestMapping(value = "/records", method = RequestMethod.POST)
    public ResponseEntity<URI> create(@RequestBody @Validated RecordCreate recordCreate, @AuthenticationPrincipal Member member) {

        String id = recordsService.save(recordCreate, member.getMemberId());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "해당하는 회원의 자격 전체 조회", notes = "해당하는 회원의 수상 내역이나 자격증을 전체 조회합니다.")
    @ApiResponse(code = 200, message = "성공")
    @RequestMapping(value = "/{memberId}/records", method = RequestMethod.GET)
    public ResponseEntity<MappingJacksonValue> retrieveAllRecordsByMember(@PathVariable String memberId) {
        List<RecordsDto> list = recordsService.findOneByMember(memberId);

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("recordId", "awardsTitle", "history", "awardsDate", "member", "fileUrl");

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("files", filter);

        MappingJacksonValue jacksonValue = new MappingJacksonValue(list);
        jacksonValue.setFilters(filterProvider);

        return ResponseEntity.ok(jacksonValue);
    }

    @ApiOperation(value = "해당하는 회원의 자격 전체 조회", notes = "해당하는 회원의 수상 내역이나 자격증을 단건 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 404, message = "존재하지 않음")
    })
    @RequestMapping(value = "/records/{recordsId}", method = RequestMethod.GET)
    public ResponseEntity<MappingJacksonValue> retrieveRecords(@PathVariable String recordsId) {

        RecordsDto recordsDto = recordsService.findOne(recordsId);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("awardsTitle", "history", "awardsDate", "member", "fileUrl");

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("files", filter);

        MappingJacksonValue jacksonValue = new MappingJacksonValue(recordsDto);
        jacksonValue.setFilters(filterProvider);

        return ResponseEntity.ok(jacksonValue);
    }

    @ApiOperation(value = "해당하는 회원의 자격 삭제", notes = "해당하는 회원의 수상 내역이나 자격증을 단건 삭제합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "해당 권한 없음")
    })
    @RequestMapping(value = "/records/{recordsId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable String recordsId, @AuthenticationPrincipal Member member) {
        recordsService.delete(recordsId, member.getMemberId());
        return ResponseEntity.ok().build();
    }

}
