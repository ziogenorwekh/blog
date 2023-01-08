package com.portfolio.blog.vo.post;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.portfolio.blog.dto.PostDto;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@JsonFilter("postResp")
public class PostResponse {
    @ApiParam(value = "포스트 UUID")
    @ApiModelProperty(example = "asejajvase-vae!eaj6234iA")
    private String postId;
    @ApiParam(value = "글 제목")
    @ApiModelProperty(example = "제목")
    private String title;
    @ApiParam(value = "글 내용")
    @ApiModelProperty(example = "내용")
    private String contents;
    @JsonFormat(pattern = "YY-MM-dd", shape = JsonFormat.Shape.STRING)
    private Date postedBy;

    @ApiParam(value = "학습 또는 작품")
    @ApiModelProperty(example = "STUDY")
    private String category;
    @ApiParam(value = "작성자")
    @ApiModelProperty(example = "lsek")
    private String postMember;

    public PostResponse(PostDto postDto) {
        this.postId = postDto.getPostId();
        this.title = postDto.getTitle();
        this.contents = postDto.getContents();
        this.postedBy = postDto.getPostedBy();
        this.postMember = postDto.getMember().getName();
        this.category = postDto.getCategory().name();
    }
}
