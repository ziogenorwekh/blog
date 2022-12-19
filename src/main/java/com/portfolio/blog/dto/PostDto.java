package com.portfolio.blog.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.portfolio.blog.domain.Post} entity
 */
@Data
public class PostDto implements Serializable {
    private Long id;
    private String postId;
    private String title;
    private String subTitle;
    private String contents;
    private Date postedBy;
    private MemberDto member;
}