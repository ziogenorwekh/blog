package com.portfolio.blog.vo.records;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class RecordsUpdate {

    @ApiModelProperty(example = "전국 코딩스타 경진대회")
    @ApiParam(value = "경진대회 타이틀", required = true)
    @NotBlank(message = "타이틀 명을 입력해주세요.")
    private String awardsTitle;

    @ApiModelProperty(example = "etc) 동상")
    @ApiParam(value = "경진대회 수상 내역 또는 참가 이력")
    @NotBlank(message = "내역을 입력해주세요.")
    private String history;

    @ApiModelProperty(example = "20-07-12")
    @ApiParam(value = "수상 일자")
    @NotBlank(message = "수상 일자를 입력해주세요.")
    private Date awardsDate;

    @ApiParam(value = "수상한 결과 사진 등")
    private String fileUrl;
}
