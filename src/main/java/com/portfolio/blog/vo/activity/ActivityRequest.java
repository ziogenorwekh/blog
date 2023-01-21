package com.portfolio.blog.vo.activity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class ActivityRequest {

    @NotBlank(message = "빈 값은 허용되지 않습니다.")
    private String name;
    private Date period;
    private String story;
    private String type;
}
