package com.portfolio.blog.vo.post;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.portfolio.blog.dto.PostDto;
import lombok.Data;

import java.util.Date;

@Data
@JsonFilter("postResp")
public class PostResponse {
    private String postId;
    private String title;
    private String subTitle;
    private String contents;
    @JsonFormat(pattern = "YY-MM-dd", shape = JsonFormat.Shape.STRING)
    private Date postedBy;
    private String postMember;

    public PostResponse(PostDto postDto) {
        this.postId = postDto.getPostId();
        this.title = postDto.getTitle();
        this.subTitle = postDto.getSubTitle();
        this.contents = postDto.getContents();
        this.postedBy = postDto.getPostedBy();
        this.postMember = postDto.getMember().getName();
    }
}
