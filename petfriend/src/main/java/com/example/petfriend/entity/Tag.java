package com.example.petfriend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags",
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_tags_name", columnNames = "name" )
        }
      )
@Getter
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "color", nullable = false, length = 20)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToMany(mappedBy = "tags")
    private Set<Task> tasks = new HashSet<>();


    @Builder
    public Tag(String name, String color, Project project) {
        this.name = name;
        this.color = color;
        this.project = project;
    }

    public void addTags(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void updateTags(String name, String color) {
        this.name = name;
        this.color = color;
    }

}
