package com.miniaspire.loan.exceptions.exceptionhandler;

import com.miniaspire.loan.exceptions.InvalidInputException;
import com.miniaspire.loan.exceptions.TechnicalUnExpectedException;
import com.miniaspire.loan.exceptions.UnAuthorisedAccessException;
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

    private static final Logger LOG = LoggerFactory.getLogger(LoanExceptionHandler.class);
    private static final String EXCEPTION = "Exception:";

    @ExceptionHandler(value
            = {RuntimeException.class})
    protected ResponseEntity<Object> handleRuntimeException(
            RuntimeException ex, WebRequest request) {

        LOG.error(EXCEPTION, ex);
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value
            = {InvalidInputException.class})
    protected ResponseEntity<Object> handleInvalidInputException(
            InvalidInputException ex, WebRequest request) {

        LOG.error(EXCEPTION, ex);
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value
            = {TechnicalUnExpectedException.class})
    protected ResponseEntity<Object> handleTechnicalUnExpectedException(
            TechnicalUnExpectedException ex, WebRequest request) {

        LOG.error(EXCEPTION, ex);
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value
            = {UnAuthorisedAccessException.class})
    protected ResponseEntity<Object> handleUnAuthorisedAccessException(
            UnAuthorisedAccessException ex, WebRequest request) {

        LOG.error(EXCEPTION, ex);
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

}
