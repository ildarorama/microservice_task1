package com.epam.microservice.task1.subtask2.cloud;

import com.epam.microservice.task1.subtask2.controller.dto.ErrorMessage;
import lombok.Data;

@Data
public class SongServiceException extends RuntimeException {
    private ErrorMessage errorMessage;

    public SongServiceException(String message) {
        super(message);
    }
}
