package com.portfolio.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Records not in database")
public class RecordNotFountException extends RuntimeException {
    public RecordNotFountException(String message) {
        super(message);
    }
}
