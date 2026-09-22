package com.pragma.capacity.infrastructure.exceptionhandler;

import com.pragma.capacity.domain.exception.CapacityNotFoundException;
import com.pragma.capacity.domain.exception.DuplicateTechnologyException;
import com.pragma.capacity.domain.exception.InvalidPaginationParameterException;
import com.pragma.capacity.domain.exception.InvalidTechnologyCountException;
import com.pragma.capacity.domain.exception.TechnologyNotFoundException;
import com.pragma.capacity.domain.exception.TechnologyServiceUnavailableException;
import com.pragma.capacity.infrastructure.exception.NoDataFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvisor {

    private static final String MESSAGE = "message";

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoDataFoundException(
            NoDataFoundException ignoredNoDataFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.NO_DATA_FOUND.getMessage()));
    }

    @ExceptionHandler(InvalidTechnologyCountException.class)
    public ResponseEntity<Map<String, String>> handleInvalidTechnologyCountException(
            InvalidTechnologyCountException ignore) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.INVALID_NUMBER_OF_TECHNOLOGIES_ASSOCIATES.getMessage()));
    }

    @ExceptionHandler(DuplicateTechnologyException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateTechnologyException(
            DuplicateTechnologyException ignore) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.DUPLICATE_TECHNOLOGY_ID.getMessage()));
    }

    @ExceptionHandler(TechnologyNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleTechnologyNotFoundException(
            TechnologyNotFoundException ignore) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.TECHNOLOGY_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(TechnologyServiceUnavailableException.class)
    public ResponseEntity<Map<String, String>> handleTechnologyServiceUnavailableException(
            TechnologyServiceUnavailableException ignore) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.TECHNOLOGY_SERVICE_UNAVAILABLE.getMessage()));
    }


    @ExceptionHandler(CapacityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCapacityNotFoundException(
            CapacityNotFoundException ignore) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.CAPACITY_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(InvalidPaginationParameterException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPaginationParameterException(
            InvalidPaginationParameterException ignore) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.INVALID_PAGINATION_PARAMETERS.getMessage()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(WebExchangeBindException exception) {
        Map<String, String> errors = exception.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null
                                ? fieldError.getDefaultMessage()
                                : ExceptionResponse.INVALID_REQUEST.getMessage(),
                        (existing, _) -> existing
                ));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
