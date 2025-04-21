package com.inventory.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
