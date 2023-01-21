package com.portfolio.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND,reason = "type is not matched.")
public class TypeNotMatchingException extends RuntimeException {
    public TypeNotMatchingException(String message) {
        super(message);
    }
}
