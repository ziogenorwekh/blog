package com.portfolio.blog.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.portfolio.blog.domain.Post} entity
 */
@Data
public class PostDto implements Serializable {
    private final Long id;
    private final String postId;
    private final String title;
    private final String subTitle;
    private final String contents;
    private final Date postedBy;
    private final MemberDto member;
}