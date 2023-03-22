package com.mms.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.mms.demo.model.ErrorResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleException(CustomException exception) {
        return new ResponseEntity<>(
                        ErrorResponse.builder().errorMessage(exception.getMessage())
                                        .errorCode(exception.getErrorCode()).build(),
                        HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Custom403Exception.class)
    public ResponseEntity<ErrorResponse> handle403Exception(Custom403Exception exception) {
        return new ResponseEntity<>(
                        ErrorResponse.builder().errorMessage(exception.getMessage())
                                        .errorCode(exception.getErrorCode()).build(),
                        HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(Exception exception) {
        return new ResponseEntity<>(
                        ErrorResponse.builder().errorMessage(exception.getMessage())
                                        .errorCode("ACCESS_FORBIDDEN").build(),
                        HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception exception) {
        return new ResponseEntity<>(
                        ErrorResponse.builder().errorMessage(exception.getMessage())
                                        .errorCode("AUTHENTICATION_FAILED").build(),
                        HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({io.jsonwebtoken.security.SignatureException.class})
    public ResponseEntity<ErrorResponse> handleSignatureException(Exception exception) {
        return new ResponseEntity<>(
                        ErrorResponse.builder().errorMessage("Invalid jwt token sent")
                                        .errorCode("INVALID TOKEN").build(),
                        HttpStatus.GATEWAY_TIMEOUT);
    }

}
