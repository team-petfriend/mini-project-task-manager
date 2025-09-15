package com.example.petfriend.repository;

import com.example.petfriend.dto.project.response.ProjectResponse;
import com.example.petfriend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByNameContainingIgnoreCase(String projectName);

}
