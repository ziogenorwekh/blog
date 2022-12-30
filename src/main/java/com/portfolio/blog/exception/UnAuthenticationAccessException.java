package com.portfolio.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "you're not access this data")
public class UnAuthenticationAccessException extends RuntimeException {
    public UnAuthenticationAccessException(String message) {
        super(message);
    }
}
