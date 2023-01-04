package com.portfolio.blog.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link com.portfolio.blog.domain.WorkUrl} entity
 */
@Data
public class WorkUrlDto implements Serializable {
    private String workUrlID;
    private String url;

}