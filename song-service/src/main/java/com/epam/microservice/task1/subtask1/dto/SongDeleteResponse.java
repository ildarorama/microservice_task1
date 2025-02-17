package com.epam.microservice.task1.subtask1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongDeleteResponse {
    public List<Long> ids;
}
