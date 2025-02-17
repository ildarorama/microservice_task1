package com.epam.microservice.task1.subtask2.controller;

import com.epam.microservice.task1.subtask2.cloud.SongServiceException;
import com.epam.microservice.task1.subtask2.controller.dto.ErrorMessage;
import com.epam.microservice.task1.subtask2.exception.SongNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;

@RestControllerAdvice(assignableTypes = ResourceController.class)
public class ResourceControllerAdvice {

    @ExceptionHandler(exception = SongNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage resourceNotFoundException(Exception ex) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage());
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

    @ExceptionHandler(exception = SongServiceException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage unexpectedException(SongServiceException ex) {
        if (ex.getErrorMessage() != null) {
            return ex.getErrorMessage();
        }
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage());
        return message;
    }


    @ExceptionHandler(exception = {MethodArgumentNotValidException.class, HandlerMethodValidationException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage argumentValidateException(Exception ex) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                "Validation error");
        message.setDetails(new HashMap<>());
        if (ex instanceof MethodArgumentNotValidException) {
            ((MethodArgumentNotValidException) ex).getFieldErrors().forEach(error ->
                    message.getDetails().put(error.getField(), error.getDefaultMessage()));
        }
        return message;
    }

}
