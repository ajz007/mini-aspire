package com.miniaspire.loan.exceptions.exceptionhandler;

import com.miniaspire.loan.exceptions.InvalidInputException;
import com.miniaspire.loan.exceptions.TechnicalUnExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class LoanExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoanExceptionHandler.class);

    @ExceptionHandler(value
            = { Exception.class })
    protected ResponseEntity<Object> handleException(
            RuntimeException ex, WebRequest request) {

        LOGGER.error(ex.toString());
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value
            = { RuntimeException.class })
    protected ResponseEntity<Object> handleRuntimeException(
            RuntimeException ex, WebRequest request) {

        LOGGER.error(ex.toString());
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value
            = { InvalidInputException.class })
    protected ResponseEntity<Object> handleInvalidInputException(
            InvalidInputException ex, WebRequest request) {

        LOGGER.error(ex.toString());
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value
            = { TechnicalUnExpectedException.class })
    protected ResponseEntity<Object> handleTechnicalUnExpectedException(
            TechnicalUnExpectedException ex, WebRequest request) {

        LOGGER.error(ex.toString());
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
