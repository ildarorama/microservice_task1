package com.epam.microservice.task1.subtask1.dto;

import com.epam.microservice.task1.subtask1.model.SongBean;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@NoArgsConstructor
public class SongBeanEntity {
    private long id;
    private String name;
    private String artist;
    private String album;
    private String duration;
    private int year;

    public SongBeanEntity(SongBean songBean) {
        this.id = songBean.getId();
        this.name = songBean.getName();
        this.artist = songBean.getArtist();
        this.album = songBean.getAlbum();
        var duration = Duration.ofSeconds(songBean.getDuration());
        int minutes = duration.toMinutesPart();
        int seconds = duration.toSecondsPart();
        this.duration = String.format("%02d:%02d", minutes, seconds);
        this.year = songBean.getYear();
    }
}
