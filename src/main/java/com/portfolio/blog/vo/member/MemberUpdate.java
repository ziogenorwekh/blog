package com.portfolio.blog.vo.member;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class MemberUpdate {

    @ApiParam(value = "사용자 이름", required = true)
    @ApiModelProperty(example = "lsek")
    @NotBlank(message = "빈 값은 허용되지 않습니다.")
    @Size(min = 3,message = "최소 세글자 이상의 이름을 입력해주세요.")
    private String name;

    @ApiParam(value = "사용자 비밀번호", required = true)
    @ApiModelProperty(example = "password1!")
    @Size(min = 4,message = "최소 네글자 이상의 비밀번호를 입력해주세요.")
    private String password;
}
