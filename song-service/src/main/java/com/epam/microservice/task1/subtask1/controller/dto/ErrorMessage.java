package com.epam.microservice.task1.subtask1.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class ErrorMessage {
    private int errorCode;
    private String errorMessage;
    private Map<String, String> details;

    public ErrorMessage(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
