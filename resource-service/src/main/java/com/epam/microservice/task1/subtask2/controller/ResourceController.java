package com.epam.microservice.task1.subtask2.controller;

import com.epam.microservice.task1.subtask2.cloud.dto.DeleteResourceResponse;
import com.epam.microservice.task1.subtask2.response.CreateSongResponse;
import com.epam.microservice.task1.subtask2.service.ResourceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping("/resources")
    public ResponseEntity<CreateSongResponse> addSong(@RequestBody byte[] file) {
        long id = resourceService.uploadSong(file);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateSongResponse(id));
    }

    @GetMapping(value = "/resources/{id}", produces = "audio/mpeg")
    public ResponseEntity<byte[]> downloadSong(@PathVariable("id") Long id) {
        byte[] content = resourceService.downloadSong(id);
        return ResponseEntity.ok(content);
    }

    @DeleteMapping("/resources")
    public ResponseEntity<DeleteResourceResponse> deleteSong(@RequestParam("id") List<Long> ids) {
        return ResponseEntity.ok(new DeleteResourceResponse(resourceService.removeSongs(ids)));
    }
}
