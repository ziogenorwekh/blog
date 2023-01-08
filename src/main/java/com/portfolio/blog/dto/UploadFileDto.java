package com.portfolio.blog.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.portfolio.blog.domain.UploadFile;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.UrlResource;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link UploadFile} entity
 */
@Data
@NoArgsConstructor
@JsonFilter("files")
public class UploadFileDto implements Serializable {
    private Long id;
    @ApiParam(value = "파일 UUID")
    @ApiModelProperty(example = "aeal;vajske-217a;kv;kase;ka;asme;k")
    private String fileId;
    @ApiParam(value = "파일명")
    @ApiModelProperty(example = "hello.png")
    private String filename;
    @ApiParam(value = "파일 확장자 타입")
    @ApiModelProperty(example = "text/plain")
    private String mimetype;
    @ApiParam(value = "파일 등록 일자")
    @ApiModelProperty(example = "22-12-03")
    private Date insertDate;
    @JsonIgnore
    private UrlResource urlResource;

    public UploadFileDto(UploadFile file, UrlResource urlResource) {
        this.id = file.getId();
        this.fileId = file.getFileId();
        this.filename = file.getFilename();
        this.mimetype = file.getMimetype();
        this.insertDate = file.getInsertDate();
        this.urlResource = urlResource;
    }
}