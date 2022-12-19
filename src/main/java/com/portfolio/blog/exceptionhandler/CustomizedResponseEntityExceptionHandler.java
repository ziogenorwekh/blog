package com.portfolio.blog.exceptionhandler;

import com.portfolio.blog.exception.*;
import com.portfolio.blog.vo.ExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;

@RestController
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                errors.get(0).getDefaultMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MemberNotFoundException.class, PostNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleNotFound(WebRequest webRequest, Exception e) {
        ExceptionResponse response = new ExceptionResponse(new Date(), e.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicate(WebRequest webRequest, Exception e) {
        ExceptionResponse response = new ExceptionResponse(new Date(), e.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnAuthenticationAccessException.class)
    public ResponseEntity<ExceptionResponse> handleUnAuthentication(WebRequest webRequest, Exception e) {
        ExceptionResponse response = new ExceptionResponse(new Date(), e.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailAuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthGone(WebRequest webRequest, Exception e) {
        ExceptionResponse response = new ExceptionResponse(new Date(), e.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.GONE);
    }
}
