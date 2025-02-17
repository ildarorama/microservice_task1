package com.epam.microservice.task1.subtask2.response;

import lombok.Data;

import java.util.List;

@Data
public class RemoveSongResponse {
    private List<Long> id;
}
