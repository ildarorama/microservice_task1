package com.epam.microservice.task1.subtask1.dto;

import com.epam.microservice.task1.subtask1.model.SongBean;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SongBeanEntity {
    private long id;
    private String name;
    private String artist;
    private String album;
    private long duration;
    private int year;

    public SongBeanEntity(SongBean songBean) {
        this.id = songBean.getId();
        this.name = songBean.getName();
        this.artist = songBean.getArtist();
        this.album = songBean.getAlbum();
        this.duration = songBean.getDuration();
        this.year = songBean.getYear();
    }
}
