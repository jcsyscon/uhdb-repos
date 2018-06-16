package com.realsnake.sample.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /** SID */
    private static final long serialVersionUID = -7845508690690806483L;

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
