package com.epam.microservice.task1.subtask2.repository;

import com.epam.microservice.task1.subtask2.model.ResourceBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceBeanRepository extends JpaRepository<ResourceBean, Long> {
}
