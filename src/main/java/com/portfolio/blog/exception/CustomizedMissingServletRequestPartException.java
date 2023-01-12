package com.portfolio.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST,reason = "mismatched file format")
public class CustomizedMissingServletRequestPartException extends MissingServletRequestPartException {
    /**
     * Constructor for MissingServletRequestPartException.
     *
     * @param requestPartName the name of the missing part of the multipart request
     */
    public CustomizedMissingServletRequestPartException(String requestPartName) {
        super(requestPartName);
    }
}
