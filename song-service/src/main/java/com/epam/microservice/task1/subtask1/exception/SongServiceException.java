package com.epam.microservice.task1.subtask1.exception;

public class SongServiceException extends RuntimeException {
    public SongServiceException(String message) {
        super(message);
    }
}