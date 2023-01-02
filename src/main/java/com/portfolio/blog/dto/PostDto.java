package com.portfolio.blog.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.portfolio.blog.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.portfolio.blog.domain.Post} entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto implements Serializable {
    private Long id;
    private String postId;
    private String title;
    private String contents;
    private Category category;
    private Date postedBy;
    private MemberDto member;
}