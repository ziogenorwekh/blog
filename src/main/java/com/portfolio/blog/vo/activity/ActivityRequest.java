package com.portfolio.blog.vo.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@ApiModel(description = "이 모델은 사용자의 활동 내역을 저장하거나 수정하는 요청입니다.")
public class ActivityRequest {

    @ApiModelProperty(example = "Java")
    @ApiParam(value = "학습한 기술이나 학습 기관 명 또는 경력 기관 명", required = true)
    @NotBlank(message = "빈 값은 허용되지 않습니다.")
    private String name;

    @ApiModelProperty(example = "22-08-12")
    @ApiParam(value = "학습 시작 기간 또는 경의 시간 기간", required = true)
    @JsonFormat(pattern = "YY-MM-dd", shape = JsonFormat.Shape.STRING)
    private Date startPeriod;


    @ApiModelProperty(example = "22-08-12")
    @ApiParam(value = "학습 종료 기간 또는 경력의 종료 기간", required = true)
    @JsonFormat(pattern = "YY-MM-dd", shape = JsonFormat.Shape.STRING)
    private Date endPeriod;


    @ApiModelProperty(example = "~~ 회사에서 ~~하고 ~~습득")
    @ApiParam(value = "정보 기입란", required = true)
    private String story;

    @NotBlank(message = "빈 값은 허용되지 않습니다.")
    @ApiParam(value = "교육인지, 학습인지, 지식 정보에 대한 타입 지정", required = true)
    @ApiModelProperty(example = "Education")
    private String type;

}
