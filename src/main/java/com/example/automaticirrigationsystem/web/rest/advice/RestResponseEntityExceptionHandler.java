package com.example.automaticirrigationsystem.web.rest.advice;

import com.example.automaticirrigationsystem.exception.BadRequestException;
import com.example.automaticirrigationsystem.exception.ResourceDoesntExistException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * A global exception handler for REST API.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
  protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {

    return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT,
        request);
  }

  @ExceptionHandler(ResourceDoesntExistException.class)
  public ResponseEntity<Object> resourceDoesntExistExceptionHandler(Exception ex) {

    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Object> badRequestExceptionHandler(Exception ex) {

    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> defaultExceptionHandler(final Exception ex, WebRequest request) {

    return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),
        HttpStatus.INTERNAL_SERVER_ERROR, request);

  }

}
