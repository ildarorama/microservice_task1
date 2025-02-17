package com.epam.microservice.task1.subtask1.service;

import com.epam.microservice.task1.subtask1.dto.SongCreateRequest;
import com.epam.microservice.task1.subtask1.exception.SongAlreadyExists;
import com.epam.microservice.task1.subtask1.exception.SongNotFoundException;
import com.epam.microservice.task1.subtask1.model.SongBean;
import com.epam.microservice.task1.subtask1.repository.SongBeanRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class SongService {
    private final SongBeanRepository songBeanRepository;

    public SongBean save(SongCreateRequest request) {
        if (songBeanRepository.findById(request.getId()).isPresent()) {
            throw new SongAlreadyExists();
        }
        var bean = new SongBean();
        bean.setId(request.getId());
        bean.setArtist(request.getArtist());
        bean.setAlbum(request.getAlbum());
        bean.setName(request.getName());
        bean.setYear(Integer.parseInt(request.getYear()));
        //bean.setDuration(request.getDuration());
        songBeanRepository.save(bean);
        return bean;
    }

    public SongBean getById(Long id) throws IllegalArgumentException {
        return songBeanRepository.findById(id).orElseThrow(() -> new SongNotFoundException("Song not found"));
    }

    public List<SongBean> getAllSongs() {
        return songBeanRepository.findAll();
    }

    public List<SongBean> deleteById(List<Long> ids) {
        var songs = songBeanRepository.findAllById(ids);
        songBeanRepository.deleteAll(songs);
        return songs;
    }

}
