package com.onboarding.customer.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ApplicationNotFoundException.class)
	 public ResponseEntity<ErrorResponse> handleApplicationNotFound(ApplicationNotFoundException ex) {
        return new ResponseEntity<>(
            ErrorResponse.buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND),
            HttpStatus.NOT_FOUND
        );
    }
}

