package com.ffreaky.foodservice.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Date;
import java.util.HashMap;

@ControllerAdvice
public class FinishFoodExceptionHandler {

    @ExceptionHandler(FinishFoodException.class)
    public ResponseEntity<ErrorMessageDto> handleFinishFoodException(final FinishFoodException exception, final WebRequest request) {
        final ErrorMessageDto errorMessage = buildErrorMessageBody(exception, request);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), errorMessage.status());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorMessageDto> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request) {
        final FinishFoodException ffException = new FinishFoodException(FinishFoodException.Type.NOT_FOUND, "Request handler not found!");
        final ErrorMessageDto body = buildErrorMessageBody(ffException, request);
        return new ResponseEntity<>(body, ffException.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageDto> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex, final WebRequest request) {
        final HashMap<String, String> argumentViolations = new HashMap<>();
        for (final FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            argumentViolations.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        final FinishFoodException ffException = new FinishFoodException(FinishFoodException.Type.BAD_REQUEST, "Method argument violations");
        final ErrorMessageDto body = buildErrorMessageBody(ffException, request, argumentViolations);
        return new ResponseEntity<>(body, ffException.getStatusCode());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessageDto> handleConstraintViolationException(final ConstraintViolationException ex, final WebRequest request) {
        final HashMap<String, String> constraintViolations = new HashMap<>();
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            constraintViolations.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        final FinishFoodException ffException = new FinishFoodException(FinishFoodException.Type.BAD_REQUEST, "JPA constraint violations");
        final ErrorMessageDto body = buildErrorMessageBody(ffException, request, constraintViolations);
        return new ResponseEntity<>(body, ffException.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDto> handleException(Exception ex, WebRequest request) {
        final FinishFoodException ffException = new FinishFoodException(FinishFoodException.Type.SERVER_ERROR, ex.getMessage());
        final ErrorMessageDto body = buildErrorMessageBody(ffException, request);
        return new ResponseEntity<>(body, ffException.getStatusCode());
    }

    private ErrorMessageDto buildErrorMessageBody(final FinishFoodException ffException, final WebRequest request) {
        return buildErrorMessageBody(ffException, request, null);
    }

    private ErrorMessageDto buildErrorMessageBody(final FinishFoodException ffException, final WebRequest request, final Object content) {
        return new ErrorMessageDto(
                new Date(),
                ffException.getStatusCode().value(),
                ffException.getDescription(),
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                content);
    }

}
