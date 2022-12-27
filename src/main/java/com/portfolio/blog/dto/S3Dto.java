package com.portfolio.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class S3Dto {
    private String uploadUrl;
    private String filename;
}
