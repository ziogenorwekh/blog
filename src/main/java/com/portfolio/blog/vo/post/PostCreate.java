package com.portfolio.blog.vo.post;

import com.portfolio.blog.domain.Category;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class PostCreate {

    @ApiParam(value = "글 제목")
    @ApiModelProperty(example = "제목")
    @NotBlank(message = "빈 값은 허용되지 않습니다.")
    @Size(min = 2, message = "최소 두글자 이상의 제목을 정해주세요.")
    private String title;

    @ApiParam(value = "글 부제목")
    @ApiModelProperty(example = "부제목")
    private String subTitle;

    @ApiParam(value = "글 내용")
    @ApiModelProperty(example = "내용")
    private String contents;

    @ApiParam(value = "학습 또는 작품")
    @ApiModelProperty(example = "STUDY")
    @NotBlank(message = "빈 값은 허용되지 않습니다.")
    private String category;

}
