package com.uet.ooadloophole.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class CustomFileNotFoundException extends RuntimeException {
    CustomFileNotFoundException(String message) {
        super(message);
    }

    CustomFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}