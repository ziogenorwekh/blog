package com.portfolio.blog.vo.member;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.portfolio.blog.dto.MemberDto;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonFilter("memberResp")
public class MemberResponse {

    @ApiModelProperty(example = "$!fjkasekkveuvkasekAASKvkisek...")
    @ApiParam(value = "등록된 회원 UUID")
    private String memberId;
    @ApiModelProperty(example = "lsek")
    @ApiParam(value = "사용자 아이디", required = true)
    private String name;

    @ApiParam(value = "사용자 이름", required = true)
    @ApiModelProperty(example = "김철수")
    private String realName;

    @ApiModelProperty(example = "lsek@naver.com")
    @ApiParam(value = "사용자 이메일")
    private String email;

    @ApiModelProperty(example = "안녕하세요. ㅁㅁㅁ입니다.")
    @ApiParam(value = "사용자 자기소개")
    private String selfIntroduce;
    @ApiModelProperty(example = "github_id")
    @ApiParam(value = "사용자 깃허브 아이디")
    private String github;

    private List<String> workUrl;

    public MemberResponse(MemberDto memberDto) {
        this.memberId = memberDto.getMemberId();
        this.name = memberDto.getName();
        this.email = memberDto.getEmail();
        this.realName = memberDto.getRealName();
        this.github = memberDto.getGithub();
        this.selfIntroduce = memberDto.getSelfIntroduce();
        this.workUrl = memberDto.getWorkUrls().stream().map(workUrlDto -> workUrlDto.getUrl()).collect(Collectors.toList());
    }
}
