package com.epam.microservice.task1.subtask2.exception;

public class ResourceServiceException extends RuntimeException {
    public ResourceServiceException(String message) {
        super(message);
    }
}