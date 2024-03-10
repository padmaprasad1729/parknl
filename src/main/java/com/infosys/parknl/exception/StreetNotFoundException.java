package com.infosys.parknl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class StreetNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public StreetNotFoundException(String message) {
        super(message);
    }
}
