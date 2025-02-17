package com.epam.microservice.task1.subtask2.cloud.dto;

import lombok.Data;

@Data
public class CreateSongRequest {
    private Long id;
    private String name;
    private String artist;
    private String album;
    private String duration;
    private String year;
}
