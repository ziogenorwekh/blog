package com.portfolio.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link com.portfolio.blog.domain.Member} entity
 */
@Data
@AllArgsConstructor
public class MemberDto implements Serializable {
    private final Long id;
    private final String email;
    private final String memberId;
    private final String name;
}