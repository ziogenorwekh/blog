package com.portfolio.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST,reason = "File type is wrong")
public class WrongFileTypeException extends RuntimeException{
    public WrongFileTypeException(String message) {
        super(message);
    }
}
