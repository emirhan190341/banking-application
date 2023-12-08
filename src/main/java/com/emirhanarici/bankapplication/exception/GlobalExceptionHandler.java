package com.emirhanarici.bankapplication.exception;

import com.emirhanarici.bankapplication.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * Global exception handler for handling various exceptions and generating error responses.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles the custom exception InsufficientBalanceException.
     *
     * @param exception The InsufficientBalanceException that was thrown.
     * @return A ResponseEntity with an error response for insufficient balance.
     */
    @ExceptionHandler(InsufficientBalanceException.class)
    protected ResponseEntity<Object> handleNotFoundException(InsufficientBalanceException exception) {

        log.error(exception.getMessage(), exception);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .statusCode(HttpStatus.NOT_MODIFIED.value())
                .status(HttpStatus.NOT_MODIFIED)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(errorResponse);
    }


    /**
     * Handles the custom exception AccountNotFoundException.
     *
     * @param exception The AccountNotFoundException that was thrown.
     * @return A ResponseEntity with an error response for account not found.
     */
    @ExceptionHandler(AccountNotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(AccountNotFoundException exception) {

        log.error(exception.getMessage(), exception);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


}
