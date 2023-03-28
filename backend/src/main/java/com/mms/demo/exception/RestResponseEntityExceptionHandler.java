package com.mms.demo.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
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

        @ExceptionHandler({ AccessDeniedException.class })
        public ResponseEntity<ErrorResponse> handleAccessDeniedException(Exception exception) {
                return new ResponseEntity<>(
                                ErrorResponse.builder().errorMessage("Access denied")
                                                .errorCode("ACCESS_FORBIDDEN").build(),
                                HttpStatus.FORBIDDEN);
        }

        // @ExceptionHandler({ AuthenticationException.class })
        // public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception
        // exception) {
        // return new ResponseEntity<>(
        // ErrorResponse.builder().errorMessage(exception.getMessage())
        // .errorCode("AUTHENTICATION_FAILED").build(),
        // HttpStatus.UNAUTHORIZED);
        // }

        @ExceptionHandler({ io.jsonwebtoken.security.SignatureException.class })
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

        @ExceptionHandler({UsernameNotFoundException.class})
        public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(Exception e, WebRequest request) {
                return new ResponseEntity<ErrorResponse>(ErrorResponse.builder().errorMessage("Username Not found")
                                .errorCode("USERNAME_NOT_FOUND").build(),
                                HttpStatus.UNAUTHORIZED);
        }

}
