package com.neueda.atm.config;

import com.neueda.atm.common.constant.ClientFailures;
import com.neueda.atm.resource.error.ClientError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class ExceptionHandlingController{

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ClientError> entityNotFound(final UnsupportedOperationException ex) {
        ClientError clientError = new ClientError();
        clientError.setErrorName(ClientFailures.POLICY_ERROR.name());
        clientError.setDetailedExplanation(ex.getMessage());
        return new ResponseEntity<>(clientError, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ClientError> securityException(final SecurityException ex) {
        ClientError clientError = new ClientError();
        clientError.setErrorName(ClientFailures.SECURITY_ERROR.name());
        clientError.setDetailedExplanation(ex.getMessage());
        return new ResponseEntity<>(clientError, HttpStatus.FORBIDDEN);

    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ClientError> resourceException(final EntityNotFoundException ex) {
        ClientError clientError = new ClientError();
        clientError.setErrorName(ClientFailures.RESOURCE_ERROR.name());
        clientError.setDetailedExplanation(ex.getMessage());
        return new ResponseEntity<>(clientError, HttpStatus.NOT_FOUND);

    }
}