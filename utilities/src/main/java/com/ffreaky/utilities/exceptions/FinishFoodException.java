package com.ffreaky.utilities.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FinishFoodException extends RuntimeException {

    public enum Type {
        AUTHENTICATION,
        AUTHORIZATION,
        BAD_REQUEST,
        ENTITY_NOT_FOUND,
        ACCOUNT_EXISTS,
        NOT_FOUND,
        SERVER_ERROR,
        INVALID_PRODUCT_TYPE
    }

    private final Type type;
    private final String description;
    private final Throwable throwable;

    public FinishFoodException(final Type type) {
        super(type.name());
        this.type = type;
        description = null;
        throwable = null;
    }

    public FinishFoodException(final Type type, final String description) {
        super(type.name() + " : " + description);
        this.type = type;
        this.description = description;
        throwable = null;
    }

    public HttpStatus getStatusCode() {
        return switch (this.type) {
            case AUTHENTICATION -> HttpStatus.UNAUTHORIZED;
            case AUTHORIZATION -> HttpStatus.FORBIDDEN;
            case BAD_REQUEST, INVALID_PRODUCT_TYPE -> HttpStatus.BAD_REQUEST;
            case ENTITY_NOT_FOUND -> HttpStatus.UNPROCESSABLE_ENTITY;
            case ACCOUNT_EXISTS -> HttpStatus.NOT_ACCEPTABLE;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    @Override
    public String toString() {
        return "FinishFoodException {" +
                "type=" + type +
                "description=" + description +
                '}';
    }
}
