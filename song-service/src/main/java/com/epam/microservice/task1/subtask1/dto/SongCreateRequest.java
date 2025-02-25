package com.epam.microservice.task1.subtask1.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SongCreateRequest {
    @NotNull(message = "Id must be non null")
    private Long id;
    @NotNull(message = "Name must not be empty")
    @NotEmpty(message = "Name must not be empty")
    private String name;
    @NotNull(message = "Artist must not be empty")
    @NotEmpty(message = "Artist must not be empty")
    private String artist;
    @NotNull(message = "Album must not be empty")
    @NotEmpty(message = "Album must not be empty")
    private String album;
    @NotNull(message = "Duration must be in the format MM:SS")
    @NotEmpty(message = "Duration must be in the format MM:SS")
    @Pattern(regexp = "^\\d{2}:[0-5]\\d$", message = "Duration must be in the format MM:SS")
    private String duration;
    @NotNull(message = "Year must be in a YYYY format")
    @NotEmpty(message = "Year must be in a YYYY format")
    @Pattern(regexp = "^\\d{4}$", message = "Year must be in a YYYY format")
    private String year;
}
