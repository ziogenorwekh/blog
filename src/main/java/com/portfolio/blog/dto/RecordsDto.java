package com.portfolio.blog.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.portfolio.blog.domain.Records;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.portfolio.blog.domain.Records} entity
 */
@Data
@JsonFilter("records")
public class RecordsDto implements Serializable {
    private String recordId;
    @ApiParam(value = "수상작 파일또는 이미지")
    private String fileUrl;
    @ApiParam(value = "수상명")
    @ApiModelProperty(example = "~~공모전")
    private String awardsTitle;
    @ApiParam(value = "수상 내역 기록")
    @ApiModelProperty(example = "~~대회 ~~부문 설명")
    private String history;
    @ApiParam(value = "수상 일자")
    @ApiModelProperty(example = "20-08-12")
    private Date awardsDate;
    @ApiParam(value = "수상자 아이디")
    @ApiModelProperty(example = "lsek")
    private String member;

    public RecordsDto(Records records, String name) {
        this.recordId = records.getRecordId();
        this.fileUrl = records.getFileUrl();
        this.awardsTitle = records.getAwardsTitle();
        this.history = records.getHistory();
        this.awardsDate = records.getAwardsDate();
        this.member = name;
    }
}