package com.portfolio.blog.vo.auth;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@JsonAutoDetect
public class EmailVerify {

    @ApiParam(value = "인증 이메일")
    @ApiModelProperty(example = "lsek@naver.com")
    @Email(message = "이메일 형식이어야 합니다.")
    private String email;

    @ApiParam(value = "인증 번호")
    @ApiModelProperty(example = "185233")
    private String number;
}
