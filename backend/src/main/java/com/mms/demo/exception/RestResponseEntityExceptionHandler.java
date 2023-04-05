package com.mms.demo.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.mms.demo.model.ErrorResponse;

import io.jsonwebtoken.io.IOException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

        @ExceptionHandler(CustomException.class)
        public ResponseEntity<ErrorResponse> handleException(CustomException exception) {
                return new ResponseEntity<>(
                                ErrorResponse.builder().errorMessage(exception.getMessage())
                                                .errorCode(exception.getErrorCode()).build(),
                                exception.getHttpStatus());
        }

        @ExceptionHandler({ AuthenticationException.class })
        public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception exception) {
                return new ResponseEntity<>(
                                ErrorResponse.builder().errorMessage(exception.getMessage())
                                                .errorCode("AUTHENTICATION_FAILED").build(),
                                HttpStatus.UNAUTHORIZED);
        }

        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(
                        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
                        WebRequest request) {
                String fullError = ex.getMessage();
                String errCode = "default message";
                int err = fullError.lastIndexOf(errCode);

                String errMessage = fullError.substring(err);
                errMessage = errMessage.replaceAll("[\\[\\](){}]", "");
                String error = errMessage.substring(errCode.length() + 1);
                return new ResponseEntity<>(
                                ErrorResponse.builder().errorMessage(error)
                                                .errorCode("INVALID_REQUEST").build(),
                                HttpStatus.BAD_REQUEST);
        }

        @Override
        protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                        HttpStatusCode status, WebRequest request) {
                return new ResponseEntity<>(ErrorResponse.builder().errorMessage("Target URL does not exists")
                                .errorCode("URL_NOT_FOUND").build(),
                                HttpStatus.BAD_REQUEST);
        }

        @Override
        protected ResponseEntity<Object> handleTypeMismatch(
                        TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
                return new ResponseEntity<>(ErrorResponse.builder().errorMessage("Error while parsing url")
                                .errorCode("URL_NOT_FOUND").build(),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler({ AccessDeniedException.class })
        public ResponseEntity<ErrorResponse> handleAccessDeniedException(Exception exception) {
                return new ResponseEntity<>(
                                ErrorResponse.builder().errorMessage("Access denied")
                                                .errorCode("ACCESS_FORBIDDEN").build(),
                                HttpStatus.FORBIDDEN);
        }

        @ExceptionHandler({ io.jsonwebtoken.security.SignatureException.class,
                        io.jsonwebtoken.MalformedJwtException.class, io.jsonwebtoken.InvalidClaimException.class,
                        io.jsonwebtoken.MissingClaimException.class, io.jsonwebtoken.IncorrectClaimException.class,
                        io.jsonwebtoken.RequiredTypeException.class, io.jsonwebtoken.ClaimJwtException.class })
        public ResponseEntity<ErrorResponse> handleSignatureException(Exception exception) {
                return new ResponseEntity<>(
                                ErrorResponse.builder().errorMessage("Invalid jwt token sent")
                                                .errorCode("INVALID_TOKEN").build(),
                                HttpStatus.GATEWAY_TIMEOUT);
        }

        @ExceptionHandler({ io.jsonwebtoken.ExpiredJwtException.class })
        public ResponseEntity<ErrorResponse> handleExpiredTokenException(Exception exception, WebRequest request) {
                return new ResponseEntity<ErrorResponse>(ErrorResponse.builder().errorMessage("Session is terminated")
                                .errorCode("TOKEN_EXPIRED").build(),
                                HttpStatus.REQUEST_TIMEOUT);
        }

        @ExceptionHandler({ UsernameNotFoundException.class })
        public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(Exception e, WebRequest request) {
                return new ResponseEntity<ErrorResponse>(ErrorResponse.builder().errorMessage("Username Not found")
                                .errorCode("USERNAME_NOT_FOUND").build(),
                                HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler({ IllegalArgumentException.class })
        public ResponseEntity<ErrorResponse> handleIllegalArgumentException(Exception e) {
                return new ResponseEntity<>(
                                ErrorResponse.builder().errorMessage(e.getMessage()).errorCode("ILLEGAL_ARGUMENT_SENT")
                                                .build(),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler({ NullPointerException.class })
        public ResponseEntity<ErrorResponse> handleNullPointerException(Exception e) {
                System.out.println(e);
                return new ResponseEntity<>(
                                ErrorResponse.builder().errorMessage("Error has occured!").errorCode("NULL_POINTER_EXCEPTION")
                                                .build(),
                                HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler({ IOException.class })
        public ResponseEntity<ErrorResponse> handleIOException(Exception e) {
                return new ResponseEntity<>(
                                ErrorResponse.builder().errorMessage(e.getMessage()).errorCode("IO_EXCEPTION").build(),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler({ DataIntegrityViolationException.class })
        public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(Exception e) {
                return new ResponseEntity<>(
                                ErrorResponse.builder().errorMessage("Error encountered")
                                                .errorCode("DATA_INTEGRITY_EXCEPTION").build(),
                                HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler({ Exception.class })
        public ResponseEntity<ErrorResponse> handleException(Exception e) {
                return new ResponseEntity<>(
                                ErrorResponse.builder().errorMessage("Some error has occured").errorCode("EXCEPTION_ENCOUNTERED")
                                                .build(),
                                HttpStatus.INTERNAL_SERVER_ERROR);
        }

}
