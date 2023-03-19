package com.mms.demo.exception;

import lombok.Data;

@Data
public class Custom403Exception extends RuntimeException {
    private String errorCode;

    public Custom403Exception(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
