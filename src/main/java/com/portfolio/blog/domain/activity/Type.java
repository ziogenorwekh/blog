package com.portfolio.blog.domain.activity;

import com.portfolio.blog.domain.Category;
import lombok.Getter;

import java.util.Optional;

public enum Type {
    EDUCATION("education"),CAREER("career"),KNOWLEDGE("knowledge");

    @Getter
    private final String value;

    Type(String value) {
        this.value = value;
    }

    public static Optional<Type> from(String value) {
        for (Type type : Type.values()) {
            if (type.getValue().equals(value)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
