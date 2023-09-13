package com.example.finishfoodserver.exceptions;

import org.springframework.beans.TypeMismatchException;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.Date;
import java.util.HashMap;

@ControllerAdvice
public class FfExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info("1111111111111111111");
        logger.info("Exception occurred: ", ex);
        return super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info("2222222222222");
        logger.info("Exception occurred: ", ex);
        return super.handleMissingServletRequestParameter(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info("3333333333333333");
        logger.info("Exception occurred: ", ex);
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    @ExceptionHandler(FfException.class)
    public ResponseEntity<Object> handleFfException(final FfException exception, final WebRequest request) {
        final HttpHeaders headers = new HttpHeaders();
        final ErrorMessage errorMessage = buildErrorMessageBody(exception.getStatusCode(), exception.getDescription(), request);
        return handleExceptionInternal(exception, errorMessage, headers, exception.getStatusCode(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        final HashMap<String, String> argumentViolations = new HashMap<>();
        for (final FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            argumentViolations.put(fieldError.getField(), fieldError.getDefaultMessage() == null ? "" : fieldError.getDefaultMessage());
        }

        final FfException exception = new FfException(FfException.Type.BAD_REQUEST, "Method argument violations");
        final ErrorMessage errorMessage = buildErrorMessageBody(exception.getStatusCode(), exception.getDescription(), request, argumentViolations);
        return handleExceptionInternal(exception, errorMessage, headers, exception.getStatusCode(), request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
        final HashMap<String, String> constraintViolations = new HashMap<>();
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            constraintViolations.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        final FfException exception = new FfException(FfException.Type.BAD_REQUEST, "JPA constraint violations");
        final ErrorMessage errorMessage = buildErrorMessageBody(exception.getStatusCode(), exception.getDescription(), request, constraintViolations);
        return handleExceptionInternal(exception, errorMessage, new HttpHeaders(), exception.getStatusCode(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> globalExceptionHandler(Exception ex, WebRequest request) {
        logger.info("Exception occurred: ", ex);
        final HttpHeaders headers = new HttpHeaders();
        final FfException exception = new FfException(FfException.Type.SERVER_ERROR, ex.getMessage());
        final ErrorMessage errorMessage = buildErrorMessageBody(exception.getStatusCode(), exception.getDescription(), request);
        return handleExceptionInternal(exception, errorMessage, headers, exception.getStatusCode(), request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info("Exception occurred: ", ex);
        final ErrorMessage body = buildErrorMessageBody(HttpStatus.NOT_FOUND, "Handler not found!", request);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    private ErrorMessage buildErrorMessageBody(final HttpStatus httpStatus, final String errorMessage, final WebRequest request) {
        return buildErrorMessageBody(httpStatus, errorMessage, request, null);
    }

    private ErrorMessage buildErrorMessageBody(final HttpStatus httpStatus, final String errorMessage, final WebRequest request, final Object content) {
        return new ErrorMessage(
                new Date(),
                httpStatus.value(),
                errorMessage,
                ((ServletWebRequest)request).getRequest().getRequestURI(),
                content);
    }

}
