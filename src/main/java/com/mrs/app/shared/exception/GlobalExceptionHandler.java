package com.mrs.app.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFondException.class)
    public ResponseEntity<EntityNotFoundError> handleGenericException(EntityNotFondException exception) {
        return new ResponseEntity<>(exception.getError(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictingEntityException.class)
    public ResponseEntity<ConflictingResourceError> handleGenericException(ConflictingEntityException exception) {
        return new ResponseEntity<>(exception.getError(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleGenericException(Exception exception) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
