package com.sap.poland.whitelist.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
public class ProcessingFailedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ProcessingFailedException(String message) {
        super(message);
    }
    
    public ProcessingFailedException(Throwable cause) {
        super(cause);
    }
    
    @Override
    public String getMessage() {
        return "<Error>" + super.getMessage() + "</Error>";
    }
}
