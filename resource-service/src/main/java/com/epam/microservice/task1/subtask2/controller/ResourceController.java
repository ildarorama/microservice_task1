package com.epam.microservice.task1.subtask2.controller;

import com.epam.microservice.task1.subtask2.cloud.dto.DeleteResourceResponse;
import com.epam.microservice.task1.subtask2.exception.ResourceServiceException;
import com.epam.microservice.task1.subtask2.exception.SongNotFoundException;
import com.epam.microservice.task1.subtask2.response.CreateSongResponse;
import com.epam.microservice.task1.subtask2.service.ResourceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

@RestController
@AllArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping("/resources")
    public ResponseEntity<CreateSongResponse> addSong(@RequestBody byte[] file) {
        long id = resourceService.uploadSong(file);

        return ResponseEntity.status(HttpStatus.OK).body(new CreateSongResponse(id));
    }

    @GetMapping(value = "/resources/{id}", produces = "audio/mpeg")
    public ResponseEntity<byte[]> downloadSong(@Valid @Min(1) @PathVariable("id") Long id) {
        byte[] content = resourceService.downloadSong(id);
        return ResponseEntity.ok(content);
    }

    @DeleteMapping("/resources")
    public ResponseEntity<DeleteResourceResponse> deleteSong(@RequestParam("id") @Length(min=1, max=200) String ids) {
        List<Long> idList;
        try {
            idList = Stream.of(StringUtils.splitByWholeSeparatorPreserveAllTokens(ids, ",")).map(Long::valueOf).toList();
        } catch (NumberFormatException e) {
            throw new ResourceServiceException("Song id list is not parsable");
        }
        var deletedBeanList = resourceService.removeSongs(idList);
        return ResponseEntity.ok(new DeleteResourceResponse(deletedBeanList));
    }
}
