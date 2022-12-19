package com.portfolio.blog.vo.member;

import com.portfolio.blog.dto.MemberDto;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class MemberResponse {

    @ApiModelProperty(example = "$!fjkasekkveuvkasekAASKvkisek...")
    @ApiParam(value = "등록된 회원 UUID")
    private String memberId;
    @ApiModelProperty(example = "lsek")
    @ApiParam(value = "사용자 이름", required = true)
    private String name;

    @ApiModelProperty(example = "lsek@naver.com")
    @ApiParam(value = "사용자 이메일")
    private String email;

    public MemberResponse(MemberDto memberDto) {
        this.memberId = memberDto.getMemberId();
        this.name = memberDto.getName();
        this.email = memberDto.getEmail();
    }
}
