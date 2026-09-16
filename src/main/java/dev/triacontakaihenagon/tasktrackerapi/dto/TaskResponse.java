package dev.triacontakaihenagon.tasktrackerapi.dto;

import dev.triacontakaihenagon.jwtauth.entity.Task;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskResponse {
    private Long id;
    private String title;
    private boolean done;
    private LocalDateTime createdAt;
    private Long userId;

    public TaskResponse(Long id, String title, boolean done, LocalDateTime createdAt, Long userId) {
        this.id = id;
        this.title = title;
        this.done = done;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public TaskResponse(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.done = task.isDone();
        this.createdAt = task.getCreatedAt();
        this.userId = task.getUser().getId();
    }
}