package com.portfolio.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link com.portfolio.blog.domain.Member} entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto implements Serializable {
    private Long id;
    private String email;
    private String memberId;
    private String name;
}