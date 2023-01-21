package com.portfolio.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT,reason = "this name already exist")
public class DuplicatedNameException extends RuntimeException {
    public DuplicatedNameException(String message) {
        super(message);
    }
}
