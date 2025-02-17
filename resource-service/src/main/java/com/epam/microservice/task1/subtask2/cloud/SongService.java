package com.epam.microservice.task1.subtask2.cloud;

import com.epam.microservice.task1.subtask2.cloud.dto.CreateSongRequest;
import com.epam.microservice.task1.subtask2.response.CreateSongResponse;
import com.epam.microservice.task1.subtask2.response.RemoveSongResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(url = "http://localhost:8081", value = "localhost:8081")
public interface SongService {

    @PostMapping(value = "/songs", produces = MediaType.APPLICATION_JSON_VALUE)
    CreateSongResponse createSong(@RequestBody CreateSongRequest request);

    @DeleteMapping(value = "/songs", produces = MediaType.APPLICATION_JSON_VALUE)
    RemoveSongResponse deleteSongs(@RequestParam("id") List<Long> id);
}
