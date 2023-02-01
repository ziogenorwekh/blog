package com.portfolio.blog.domain.activity;

import lombok.Getter;

import java.util.Locale;
import java.util.Optional;

public enum Type {
    EDUCATION("EDUCATION"),CAREER("CAREER"),KNOWLEDGE("KNOWLEDGE");

    @Getter
    private final String typeValue;

    Type(String typeValue) {
        this.typeValue = typeValue;
    }

    public static Optional<Type> from(String typeValue) {
        for (Type type : Type.values()) {
            if (type.getTypeValue().equals(typeValue)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
