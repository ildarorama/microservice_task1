package com.epam.microservice.task1.subtask2.service;

import com.epam.microservice.task1.subtask2.cloud.SongService;
import com.epam.microservice.task1.subtask2.cloud.SongServiceException;
import com.epam.microservice.task1.subtask2.cloud.dto.CreateSongRequest;
import com.epam.microservice.task1.subtask2.exception.SongNotFoundException;
import com.epam.microservice.task1.subtask2.model.ResourceBean;
import com.epam.microservice.task1.subtask2.repository.ResourceBeanRepository;
import com.epam.microservice.task1.subtask2.response.RemoveSongResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ResourceService {
    private final ResourceBeanRepository resourceBeanRepository;

    private final SongService songService;

    public List<Long> removeSongs(List<Long> ids) {
        var songs = resourceBeanRepository.findAllById(ids);
        if (songs.isEmpty()) {
            return Collections.emptyList();
        }
        resourceBeanRepository.deleteAll(songs);

        RemoveSongResponse response = songService.deleteSongs(songs.stream().map(ResourceBean::getId).collect(Collectors.toList()));
        return response.getIds();
    }

    public byte[] downloadSong(Long id) {
        return resourceBeanRepository.findById(id).map(ResourceBean::getFile).orElseThrow(() ->
                new SongNotFoundException("Resource with ID=" + id + " not found")
        );
    }

    @Transactional
    public long uploadSong(byte[] file) {
        ResourceBean bean = new ResourceBean();
        try {
            BodyContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            ParseContext pcontext = new ParseContext();

            Mp3Parser parser = new Mp3Parser();
            parser.parse(new ByteArrayInputStream(file), handler, metadata, pcontext);

            String title = metadata.get("dc:title");
            String artist = metadata.get("xmpDM:artist");
            String album = metadata.get("xmpDM:album");
            String year = metadata.get("xmpDM:releaseDate");
            String recordDuration = metadata.get("xmpDM:duration");
            var durationExec = Math.round(Double.parseDouble(recordDuration));
            int minutes = Duration.ofSeconds(durationExec).toMinutesPart();
            int seconds = Duration.ofSeconds(durationExec).toSecondsPart();
            String duration = String.format("%02d:%02d", minutes, seconds);

            bean.setFile(file);
            resourceBeanRepository.save(bean);

            CreateSongRequest request = new CreateSongRequest();
            request.setId(bean.getId());
            request.setYear(year);
            request.setName(title);
            request.setArtist(artist);
            request.setAlbum(album);
            request.setDuration(duration);
            songService.createSong(request);
        } catch (Exception e) {
            if (e instanceof SongServiceException) {
                throw (SongServiceException) e;
            }
            throw new SongServiceException("Invalid audio file format. Only MP3 is allowed");
        }
        return bean.getId();
    }
}
