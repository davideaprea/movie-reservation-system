package com.mrs.app.shared.exception;

import com.mrs.app.payment.exception.PaymentGatewayException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
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
        System.out.println(exception);
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handle(Exception exception) {
        System.out.println(exception);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
