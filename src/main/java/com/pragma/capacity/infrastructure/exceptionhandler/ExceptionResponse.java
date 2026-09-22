package com.pragma.capacity.infrastructure.exceptionhandler;

import lombok.Getter;

@Getter
public enum ExceptionResponse {
    NO_DATA_FOUND("No data found for the requested petition"),
    INVALID_REQUEST("Invalid request body"),
    INVALID_NUMBER_OF_TECHNOLOGIES_ASSOCIATES("Invalid number of technologies associated with the requested petition"),
    DUPLICATE_TECHNOLOGY_ID("Duplicate technology id"),
    TECHNOLOGY_NOT_FOUND("One or more technologies do not exist"),
    TECHNOLOGY_SERVICE_UNAVAILABLE("Technology service is unavailable, please try again later"),
    ;

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

}
