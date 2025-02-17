package com.epam.microservice.task1.subtask2.service;

import com.epam.microservice.task1.subtask2.cloud.SongService;
import com.epam.microservice.task1.subtask2.cloud.SongServiceException;
import com.epam.microservice.task1.subtask2.cloud.dto.CreateSongRequest;
import com.epam.microservice.task1.subtask2.exception.SongNotFoundException;
import com.epam.microservice.task1.subtask2.model.ResourceBean;
import com.epam.microservice.task1.subtask2.repository.ResourceBeanRepository;
import com.epam.microservice.task1.subtask2.response.CreateSongResponse;
import com.epam.microservice.task1.subtask2.response.RemoveSongResponse;
import lombok.AllArgsConstructor;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ResourceService {
    private final ResourceBeanRepository resourceBeanRepository;

    private final SongService songService;

    public List<Long> removeSongs(List<Long> ids) {
        var songs = resourceBeanRepository.findAllById(ids);
        for (ResourceBean song : songs) {
            try {
                File f = new File(song.getFilePath());
                if (f.exists()) {
                    f.delete();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        resourceBeanRepository.deleteAll(songs);

        RemoveSongResponse response = songService.deleteSongs(songs.stream().map(ResourceBean::getId).collect(Collectors.toList()));
        return response.getId();
    }

    public byte[] downloadSong(Long id) {
        var bean = resourceBeanRepository.findById(id).orElseThrow(() ->
                new SongNotFoundException("Song with such id is not found")
        );

        var file = new File(bean.getFilePath());
        if (!file.exists()) {
            throw new SongNotFoundException("Resource file not found");
        }
        try (InputStream ios = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream(Math.toIntExact(file.length()))) {
            ios.transferTo(bos);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new SongNotFoundException("Resource can not be downloaded");
        }
    }

    public long uploadSong(byte[] file) {
        File songFile = saveFileToDisk(file);

        ResourceBean bean = new ResourceBean();
        try {
            AudioFile audioFile = AudioFileIO.read(songFile);
            Tag tag = audioFile.getTag();

            String title = tag.getFirst(FieldKey.TITLE);
            String artist = tag.getFirst(FieldKey.ARTIST);
            String album = tag.getFirst(FieldKey.ALBUM);
            String year = tag.getFirst(FieldKey.YEAR);
            int minutes = Duration.ofSeconds(audioFile.getAudioHeader().getTrackLength()).toMinutesPart();
            int seconds = Duration.ofSeconds(audioFile.getAudioHeader().getTrackLength()).toSecondsPart();
            String duration = String.format("%02d:%02d", minutes, seconds);

                    bean.setFilePath(songFile.getAbsolutePath());
            resourceBeanRepository.save(bean);

            CreateSongRequest request = new CreateSongRequest();
            request.setId(bean.getId());
            request.setYear(year);
            request.setName(title);
            request.setArtist(artist);
            request.setAlbum(album);
            request.setDuration(duration);
            CreateSongResponse response = songService.createSong(request);
            System.out.println(response);
        } catch (Exception e) {
            songFile.delete();
            if (e instanceof SongServiceException) {
                throw (SongServiceException)e;
            }
            throw new IllegalArgumentException(e);
        }

        return bean.getId();
    }

    private File saveFileToDisk(byte[] file) {
        try {
            File songFile = File.createTempFile("song_", ".mp3", new File("."));

            try (InputStream inputStream = new ByteArrayInputStream(file);
                 FileOutputStream fos = new FileOutputStream(songFile)) {
                inputStream.transferTo(fos);

                return songFile;
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
