package io.github.davideaprea.exception;

import com.mrs.app.payment.exception.PaymentGatewayException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

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
        return new ResponseEntity<>(exception.getError(), HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(EntityNotFondException.class)
    public ResponseEntity<EntityNotFoundError> handle(EntityNotFondException exception) {
        return new ResponseEntity<>(exception.getError(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictingEntityException.class)
    public ResponseEntity<ConflictingResourceError<?>> handle(ConflictingEntityException exception) {
        return new ResponseEntity<>(exception.getError(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PaymentGatewayException.class)
    public ResponseEntity<Void> handle(PaymentGatewayException exception) {
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handle(Exception exception) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
