package org.crok4it.em.exception;

import org.crok4it.em.dto.ErrorResponse;
import org.crok4it.em.dto.ErrorResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class EMExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    protected final ResponseEntity<ErrorResponses> handleResourceNotFoundException(ResourceNotFoundException e,
                                                                                   WebRequest request) {
        ErrorResponses errorResponses = new ErrorResponses()
                .code(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false).replaceAll("uri=", ""))
                .errorCode(e.getErrorCode().toString())
                .errors(List.of(new ErrorResponse()
                        .message(e.getLocalizedMessage())));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponses);
    }

    @ExceptionHandler(ConflictException.class)
    protected final ResponseEntity<ErrorResponses> handleConflictException(ConflictException e,
                                                                           WebRequest request) {
        ErrorResponses errorResponses = new ErrorResponses()
                .code(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false).replaceAll("uri=", ""))
                .errorCode(e.getErrorCode().toString())
                .errors(List.of(new ErrorResponse()
                        .message(e.getLocalizedMessage())));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponses);
    }

    @ExceptionHandler(BadRequestException.class)
    protected final ResponseEntity<ErrorResponses> handleBadRequestException(BadRequestException e,
                                                                             WebRequest request) {
        ErrorResponses errorResponses = new ErrorResponses()
                .code(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false).replaceAll("uri=", ""))
                .errorCode(e.getErrorCode().toString())
                .errors(List.of(new ErrorResponse()
                        .message(e.getLocalizedMessage())));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponses);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorResponses errorResponses = new ErrorResponses()
                .code(HttpStatus.BAD_REQUEST.value())
                .path(request.getDescription(false).replaceAll("uri=", ""))
                .timestamp(LocalDateTime.now());
        List<ErrorResponse> errors = new ArrayList<>();
        e.getBindingResult().getFieldErrors()
                .forEach(fieldError -> errors.add(
                        new ErrorResponse()
                                .field(fieldError.getField())
                                .message(fieldError.getDefaultMessage())));
        errorResponses.setErrors(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponses);
    }


}
