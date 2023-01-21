package com.portfolio.blog.dto;

import com.portfolio.blog.domain.activity.Activity;
import com.portfolio.blog.domain.activity.Type;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link Activity} entity
 */
@Data
public class ActivityDto implements Serializable {
    private String activityId;
    private String name;
    private String story;
    private Date period;
    private Type activityType;
}