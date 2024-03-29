package com.infosys.parknl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UnRegistrationNotPossibleException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnRegistrationNotPossibleException(String message) {
        super(message);
    }

}