package com.portfolio.blog.exceptionhandler;

import com.portfolio.blog.exception.*;
import com.portfolio.blog.vo.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        for (ObjectError e : errors) {
            log.info(e.getDefaultMessage());
        }
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                errors.get(0).getDefaultMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MemberNotFoundException.class, PostNotFoundException.class,
            FileNotFoundException.class, CategoryNotMatchingException.class, RecordNotFountException.class})
    public ResponseEntity<ExceptionResponse> handleNotFound(WebRequest webRequest, Exception e) {
        ExceptionResponse response = new ExceptionResponse(new Date(), e.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ExceptionResponse> handleMaxFileSize(WebRequest webRequest) {
        ExceptionResponse response = new ExceptionResponse(new Date(),
                "Maximum file size is 20MB.", webRequest.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(WrongFileTypeException.class)
    public ResponseEntity<ExceptionResponse> handleWrongFileType(WebRequest webRequest, Exception e) {
        ExceptionResponse response = new ExceptionResponse(new Date(),
                e.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DuplicatedEmailException.class, DuplicatedNameException.class})
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

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ExceptionResponse> handleIO(WebRequest webRequest) {
        ExceptionResponse response = new ExceptionResponse(new Date(), "aws error",
                webRequest.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
