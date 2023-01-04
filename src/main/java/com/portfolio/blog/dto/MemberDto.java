package com.portfolio.blog.dto;

import com.portfolio.blog.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A DTO for the {@link com.portfolio.blog.domain.Member} entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto implements Serializable {
    private Long id;
    private String email;
    private String memberId;
    private String name;
    private String realName;
    private String selfIntroduce;
    private List<WorkUrlDto> workUrls;

    private String github;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.memberId = member.getMemberId();
        this.name = member.getName();
        this.github = member.getGithub();
        this.realName = member.getRealName();
        this.selfIntroduce = member.getSelfIntroduce();
        this.workUrls = member.getWorkUrls().stream().map(workUrl -> new ModelMapper()
                .map(workUrl,WorkUrlDto.class)).collect(Collectors.toList());
    }
}