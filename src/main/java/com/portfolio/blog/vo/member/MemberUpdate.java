package com.portfolio.blog.vo.member;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class MemberUpdate {

    @ApiParam(value = "사용자 아이디", required = true)
    @ApiModelProperty(example = "lsek")
    @NotBlank(message = "빈 값은 허용되지 않습니다.")
    @Size(min = 3,message = "최소 세글자 이상의 이름을 입력해주세요.")
    private String name;

    @ApiParam(value = "사용자 이름", required = true)
    @ApiModelProperty(example = "김철수")
    @NotBlank(message = "이름을 입력해주세요")
    @Size(min = 2, message = "최소 두 글자 이름을 입력해주시길 바랍니다.")
    private String realName;

    @ApiParam(value = "사용자 깃허브 아이디")
    @ApiModelProperty(example = "github_id")
//    @NotBlank(message = "깃허브 아이디를 입력해주세요")
    private String github;

    @ApiModelProperty(example = "안녕하세요. ㅁㅁㅁ입니다.")
    @ApiParam(value = "사용자 자기소개")
    private String selfIntroduce;

    @ApiModelProperty(example = "http://127.0.0.1/work~")
    @ApiParam(value = "사용자 작품 URL")
    private String url;


}
