package com.epam.microservice.task1.subtask1.controller;

import com.epam.microservice.task1.subtask1.controller.dto.ErrorMessage;
import com.epam.microservice.task1.subtask1.exception.SongAlreadyExists;
import com.epam.microservice.task1.subtask1.exception.SongNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;
import java.util.HashMap;

@RestControllerAdvice
public class SongServiceControllerAdvice {

    @ExceptionHandler(exception = SongNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage resourceNotFoundException(Exception ex) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage());
        return message;
    }

    @ExceptionHandler(exception = SongAlreadyExists.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage resourceAlreadyExists(Exception ex) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.CONFLICT.value(),
                "Metadata for this ID already exists");
        return message;
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage unexpectedException(Exception ex) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage());
        return message;
    }

    @ExceptionHandler(exception = {MethodArgumentNotValidException.class, HandlerMethodValidationException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage validationException(Exception ex) {

        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                "Validation error");
        if (ex instanceof HandlerMethodValidationException) {
            for (ParameterValidationResult parameterValidationResult : ((HandlerMethodValidationException) ex).getParameterValidationResults()) {
                message.setDetails(new HashMap<>());
                parameterValidationResult.getResolvableErrors().forEach(error ->
                        message.getDetails().put(parameterValidationResult.getMethodParameter().getParameterName(), error.getDefaultMessage())
                );
            }
        } else if (ex instanceof MethodArgumentNotValidException) {
            message.setDetails(new HashMap<>());
            ((MethodArgumentNotValidException)ex).getFieldErrors().forEach(error ->
                    message.getDetails().put(error.getField(), error.getDefaultMessage())
            );
        }
        return message;
    }
}
