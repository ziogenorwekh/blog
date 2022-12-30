package com.portfolio.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.GONE, reason = "expired")
public class EmailAuthenticationException extends RuntimeException {
    public EmailAuthenticationException(String message) {
        super(message);
    }
}
