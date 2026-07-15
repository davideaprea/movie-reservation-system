package com.mrs.shared.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<FieldValidationError>> handle(MethodArgumentNotValidException exception) {
        List<FieldValidationError> response = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(error -> new FieldValidationError.ValidationDetail(
                                        error.getCode(),
                                        error.getDefaultMessage()
                                ),
                                Collectors.toList()
                        )
                )).entrySet().stream()
                .map(entry -> new FieldValidationError(
                        entry.getKey(),
                        entry.getValue()
                )).toList();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DomainRequirementException.class)
    public ResponseEntity<DomainRequirementError> handle(DomainRequirementException exception) {
        return new ResponseEntity<>(new DomainRequirementError(
                exception.getReason(),
                exception.getFieldName()
        ), HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<EntityNotFoundError> handle(EntityNotFoundException exception) {
        return new ResponseEntity<>(new EntityNotFoundError(
                exception.getRequestedEntity().getSimpleName(),
                exception.getUsedParams()
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictingEntityException.class)
    public ResponseEntity<ConflictingResourceError<?>> handle(ConflictingEntityException exception) {
        return new ResponseEntity<>(new ConflictingResourceError<>(
                exception.getConflictingResources(),
                exception.getViolatingFields(),
                exception.getReason()
        ), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<ConflictingResourceError<?>> handle(UnauthorizedOperationException exception) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handle(Exception exception) {
        log.error("An unexpected error occurred: {}", exception.toString());

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
