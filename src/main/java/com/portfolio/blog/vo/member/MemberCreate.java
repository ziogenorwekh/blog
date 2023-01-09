package com.portfolio.blog.vo.member;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

import javax.validation.constraints.*;

import lombok.Data;

@Data
public class MemberCreate {

    @ApiModelProperty(example = "lsek@naver.com")
    @ApiParam(value = "사용자 이메일", required = true)
    @Email(message = "이메일 형식이어야 합니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @ApiParam(value = "사용자 아이디", required = true)
    @ApiModelProperty(example = "lsek")
    @NotBlank(message = "빈 값은 허용되지 않습니다.")
    @Size(min = 3, message = "최소 세글자 이상의 아이디를 입력해주세요.")
    private String name;

    @ApiParam(value = "사용자 이름", required = true)
    @ApiModelProperty(example = "김철수")
    @NotBlank(message = "이름을 입력해주세요")
    @Size(min = 2, message = "최소 두 글자 이름을 입력해주시길 바랍니다.")
    private String realName;


    @ApiParam(value = "사용자 비밀번호", required = true)
    @ApiModelProperty(example = "password1!")
    @Size(min = 4, message = "최소 네글자 이상의 비밀번호를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z\\\\d`~!@#$%^&*()-_=+]{4,16}$", message = "특수 문자는 적어도 하나 이상 포함되어야 합니다.")
    private String password;

}
