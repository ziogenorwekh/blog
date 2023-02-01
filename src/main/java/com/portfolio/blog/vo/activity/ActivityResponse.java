package com.portfolio.blog.vo.activity;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.portfolio.blog.domain.activity.Type;
import lombok.Data;

import java.util.Date;

@JsonFilter("activity")
@Data
public class ActivityResponse {

    private String activityId;
    private String name;
    private String story;
    private Date startPeriod;
    private Date endPeriod;
    private Type activityType;
}
