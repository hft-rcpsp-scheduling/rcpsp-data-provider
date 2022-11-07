package com.hft.provider.controller;

import com.hft.provider.controller.model.Error;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

// https://developer.mozilla.org/en-US/docs/Web/HTTP/Status
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class.getName());
    private final boolean printStackTrace;

    public GlobalExceptionHandler(@Value("${logging.level.root}") String logLevel) {
        this.printStackTrace = logLevel.equalsIgnoreCase("DEBUG");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleUncaught(HttpServletRequest request, Exception exception) {
        LOGGER.severe(exception.getClass().getSimpleName() + ": " + exception.getMessage() + " (from: " + request.getRequestURI() + ")");
        exception.printStackTrace();
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, request, exception);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Error> handleMessageNotReadable(HttpServletRequest request, Exception exception) {
        logHandledException(request, exception);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, request, exception);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Error> handleRequestMethodNotSupported(HttpServletRequest request, Exception exception) {
        logHandledException(request, exception);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, request, exception);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Error> handleSQLException(HttpServletRequest request, Exception exception) {
        logHandledException(request, exception);
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, request, exception);
    }


    @ExceptionHandler(IOException.class)
    public ResponseEntity<Error> handleIOException(HttpServletRequest request, Exception exception) {
        logHandledException(request, exception);
        return buildErrorResponse(HttpStatus.NOT_FOUND, request, exception);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Error> handleNoSuchElement(HttpServletRequest request, Exception exception) {
        logHandledException(request, exception);
        return buildErrorResponse(HttpStatus.NOT_FOUND, request, exception);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Error> handleEntityNotFound(HttpServletRequest request, Exception exception) {
        logHandledException(request, exception);
        return buildErrorResponse(HttpStatus.NOT_FOUND, request, exception);
    }

    @ExceptionHandler(InvalidObjectException.class)
    public ResponseEntity<Error> handleInvalidObject(HttpServletRequest request, Exception exception) {
        logHandledException(request, exception);
        return buildErrorResponse(HttpStatus.NOT_ACCEPTABLE, request, exception);
    }

    // === PRIVATE =====================================================================================================

    private ResponseEntity<Error> buildErrorResponse(HttpStatus httpStatus, HttpServletRequest request, Exception exception) {
        return ResponseEntity.status(httpStatus).body(new Error(httpStatus, request, exception));
    }

    private void logHandledException(HttpServletRequest request, Exception exception) {
        LOGGER.warning(exception.getClass().getSimpleName() + ": "
                + exception.getMessage()
                + " (from: " + request.getRequestURI() + ")");
        if (printStackTrace) {
            exception.printStackTrace();
        }
    }
}
