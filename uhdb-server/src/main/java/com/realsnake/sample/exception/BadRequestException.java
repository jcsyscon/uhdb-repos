package com.realsnake.sample.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    /** SID */
    private static final long serialVersionUID = 5543302874375953249L;

    public BadRequestException(String message) {
        super(message);
    }

}
