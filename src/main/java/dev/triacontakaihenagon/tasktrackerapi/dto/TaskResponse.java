package dev.triacontakaihenagon.tasktrackerapi.dto;

import dev.triacontakaihenagon.tasktrackerapi.entity.Task;
import dev.triacontakaihenagon.tasktrackerapi.entity.TaskPriority;
import dev.triacontakaihenagon.tasktrackerapi.entity.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskResponse {
    private Long id;
    private String title;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private TaskPriority priority;
    private Long userId;

    public TaskResponse(Long id, String title, TaskStatus status, LocalDateTime createdAt, TaskPriority priority, Long userId) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.createdAt = createdAt;
        this.priority = priority;
        this.userId = userId;
    }

    public TaskResponse(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.status = task.getStatus();
        this.createdAt = task.getCreatedAt();
        this.priority = task.getPriority();
        this.userId = task.getUser().getId();
    }
}