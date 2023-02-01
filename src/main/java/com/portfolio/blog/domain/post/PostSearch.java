package com.portfolio.blog.domain.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostSearch {

    private String memberId;
    private String postId;
}
