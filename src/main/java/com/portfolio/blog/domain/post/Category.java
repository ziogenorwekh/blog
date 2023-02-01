package com.portfolio.blog.domain.post;


import lombok.Getter;

import java.util.Optional;

public enum Category {
    WORK("WORK"),STUDY("STUDY");

    @Getter
    private final String value;

    Category(String value) {
        this.value = value;
    }

    public static Optional<Category> from(String value) {
        for (Category category : Category.values()) {
            if (category.getValue().equals(value)) {
                return Optional.of(category);
            }
        }
        return Optional.empty();
    }

}
