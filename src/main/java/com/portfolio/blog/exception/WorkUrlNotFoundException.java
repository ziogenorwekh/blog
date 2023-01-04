package com.portfolio.blog.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "workUrl not in database")
public class WorkUrlNotFoundException extends RuntimeException {
    public WorkUrlNotFoundException(String message) {
        super(message);
    }
}
