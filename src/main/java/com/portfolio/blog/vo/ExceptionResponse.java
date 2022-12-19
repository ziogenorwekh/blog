package com.portfolio.blog.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionResponse {

    @JsonFormat(pattern = "YY-MM-dd", shape = JsonFormat.Shape.STRING)
    private Date date;
    private String message;
    private String errorUri;
}
