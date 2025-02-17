package com.epam.microservice.task1.subtask2.cloud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteResourceResponse {
    private List<Long> ids;
}
