package com.crewmeister.challenge.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

/**
 * Global exception handler for the application.
 * Handles and centralizes all exceptions thrown across controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles cases where an expected element is not found (e.g., in the database).
     *
     * @param ex the exception thrown
     * @return HTTP 404 Not Found
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleElementNotFound(NoSuchElementException ex) {
        logger.warn("NoSuchElementException caught: {}", ex.getMessage());
        return ResponseEntity.notFound().build();
    }

    /**
     * Handles all general exceptions not explicitly caught by other handlers.
     *
     * @param ex the exception thrown
     * @return HTTP 500 Internal Server Error with the error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneral(Exception ex) {
        logger.error("Unhandled exception caught: {}", ex.getMessage(), ex);
        return ResponseEntity.internalServerError().body("Internal server error: " + ex.getMessage());
    }
}
