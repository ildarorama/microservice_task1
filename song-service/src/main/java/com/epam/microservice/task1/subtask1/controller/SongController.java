package com.epam.microservice.task1.subtask1.controller;

import com.epam.microservice.task1.subtask1.dto.SongBeanEntity;
import com.epam.microservice.task1.subtask1.dto.SongCreateRequest;
import com.epam.microservice.task1.subtask1.dto.SongCreatedResponse;
import com.epam.microservice.task1.subtask1.dto.SongDeleteResponse;
import com.epam.microservice.task1.subtask1.exception.SongNotFoundException;
import com.epam.microservice.task1.subtask1.model.SongBean;
import com.epam.microservice.task1.subtask1.service.SongService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
@AllArgsConstructor
public class SongController {
    private final SongService songService;

    @GetMapping("/songs")
    public ResponseEntity<List<SongBeanEntity>> getAllSongs() {
        return ResponseEntity.ok(songService.getAllSongs().stream().map(SongBeanEntity::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/songs/{id}")
    public ResponseEntity<SongBeanEntity> getSongById(@PathVariable @Valid @Min(1) Long id) {
        return ResponseEntity.ok(new SongBeanEntity(songService.getById(id)));
    }

    @DeleteMapping("/songs")
    public ResponseEntity<SongDeleteResponse> deleteById(@RequestParam("id") @Length(min = 1, max = 200, message = "Length of string must be > 0 and <= 200")
                                                         String ids) {
        List<Long> idList;
        try {
            idList = Stream.of(StringUtils.splitByWholeSeparatorPreserveAllTokens(ids, ",")).map(Long::valueOf).toList();
        } catch (NumberFormatException e) {
            log.error("Error while parsing ids: {}", ids);
            throw new SongNotFoundException("Song id list is not parsable");
        }
        List<SongBean> deletedBean = songService.deleteById(idList);

        return ResponseEntity.ok(new SongDeleteResponse(
                deletedBean.stream().map(SongBean::getId).collect(Collectors.toList())));
    }

    @PostMapping("/songs")
    public ResponseEntity<SongCreatedResponse> addSong(@RequestBody @Valid SongCreateRequest bean) {
        SongBean song = songService.save(bean);
        return ResponseEntity.ok(new SongCreatedResponse(song.getId()));
    }
}
