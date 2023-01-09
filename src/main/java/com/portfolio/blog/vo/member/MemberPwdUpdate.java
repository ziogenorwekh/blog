package com.portfolio.blog.vo.member;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@JsonAutoDetect
public class MemberPwdUpdate {

    @ApiParam(value = "현재 비밀번호", required = true)
    @ApiModelProperty(example = "password1!")
    private String currentPassword;

    @ApiParam(value = "사용자 비밀번호", required = true)
    @ApiModelProperty(example = "password1!")
    @Size(min = 4, message = "최소 네글자 이상의 비밀번호를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z\\\\d`~!@#$%^&*()-_=+]{4,16}$", message = "특수 문자는 적어도 하나 이상 포함되어야 합니다.")
    private String password;

}
