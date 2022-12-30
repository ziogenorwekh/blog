package com.portfolio.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND,reason = "category is not matched.")
public class CategoryNotMatchingException extends RuntimeException {
    public CategoryNotMatchingException(String message) {
        super(message);
    }
}
