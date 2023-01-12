package com.portfolio.blog.api;

import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.portfolio.blog.dto.UploadFileDto;
import com.portfolio.blog.service.UploadFileService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FileResource {

    private final UploadFileService uploadFileService;

    @ApiOperation(value = "이미지 저장", notes = "이미지을 저장합니다.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "저장 성공"),
            @ApiResponse(code = 500, message = "aws 버킷 에러"),
            @ApiResponse(code = 400, message = "지원되지 않는 형식")
    })
    @RequestMapping(value = "/images", method = RequestMethod.POST, produces = {
            MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE
    })
    public ResponseEntity<URI> saveImages(@RequestPart(value = "upload") @ApiIgnore MultipartFile upload) {
        String id = uploadFileService.save(upload);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).build();
    }


    @SneakyThrows(IOException.class)
    @ApiOperation(value = "이미지 조회", notes = "UUID 값에서 이미지을 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "조회 성공"),
            @ApiResponse(code = 404, message = "해당 파일 존재하지 않음"),
    })
    @RequestMapping(value = "/images/{fileId}", method = RequestMethod.GET, produces = {
            MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE
    })
    public ResponseEntity<?> retrieveFiles(@PathVariable String fileId) {
        UploadFileDto fileDto = uploadFileService.findOne(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileDto.getMimetype()))
                .body(IOUtils.toByteArray(fileDto.getUrlResource().getInputStream()));
    }


    /**
     * Test
     *
     * @return
     */
    @RequestMapping(value = "/files", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAll() {
        uploadFileService.deleteAll();
        return ResponseEntity.ok().build();
    }

    //    딱히 필요없는?
    @ApiOperation(value = "파일 전체 조회", notes = "파일의 정보를 전체 조회합니다.")
    @ApiResponse(code = 200, message = "조회 성공")
    @RequestMapping(value = "/files", method = RequestMethod.GET)
    public ResponseEntity<MappingJacksonValue> retrieveAllFiles() {

        List<UploadFileDto> fileDtos = uploadFileService.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("fileId", "filename", "mimetype", "insertDate");

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("files", filter);

        MappingJacksonValue jacksonValue = new MappingJacksonValue(fileDtos);
        jacksonValue.setFilters(filterProvider);

        return ResponseEntity.ok(jacksonValue);
    }

    @ApiOperation(value = "이미지 삭제", notes = "이미지를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "삭제 성공"),
            @ApiResponse(code = 404, message = "해당 파일 존재하지 않음"),
    })
    @RequestMapping(value = "/images/{fileId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable String fileId) {
        uploadFileService.delete(fileId);
        return ResponseEntity.ok().build();
    }


}
