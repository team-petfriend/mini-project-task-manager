package com.example.petfriend.repository;

import com.example.petfriend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByProjectIdAndName(Long projectId, String name);
    List<Tag> findAllByProjectId(Long projectId);
}
