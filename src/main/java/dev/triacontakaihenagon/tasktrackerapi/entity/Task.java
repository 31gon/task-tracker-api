package dev.triacontakaihenagon.tasktrackerapi.entity;

import dev.triacontakaihenagon.tasktrackerapi.dto.TaskRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "tasks")
public class Task {
    public Task() {
    }

    public Task(TaskRequest request) {
        this.title = request.getTitle();
        this.done = request.isDone();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private boolean done;
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}