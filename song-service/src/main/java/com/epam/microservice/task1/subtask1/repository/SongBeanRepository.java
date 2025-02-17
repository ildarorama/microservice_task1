package com.epam.microservice.task1.subtask1.repository;

import com.epam.microservice.task1.subtask1.model.SongBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongBeanRepository extends JpaRepository<SongBean, Long> {
}
