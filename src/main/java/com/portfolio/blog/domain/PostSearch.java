package com.portfolio.blog.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostSearch {
    private String memberId;
    private String postId;


}
