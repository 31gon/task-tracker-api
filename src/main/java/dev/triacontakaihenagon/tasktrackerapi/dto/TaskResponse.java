package dev.triacontakaihenagon.tasktrackerapi.dto;

import dev.triacontakaihenagon.tasktrackerapi.entity.TaskPriority;
import dev.triacontakaihenagon.tasktrackerapi.entity.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class TaskResponse {
    private Long id;
    private String title;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private TaskPriority priority;
    private Long userId;
    private Long categoryId;
    private List<Long> commentsId;
    private List<Long> labelsId;

    public TaskResponse() {}

    public TaskResponse(
            Long id,
            String title,
            TaskStatus status,
            LocalDateTime createdAt,
            TaskPriority priority,
            Long userId, Long categoryId,
            List<Long> commentsId,
            List<Long> labelsId
    ) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.createdAt = createdAt;
        this.priority = priority;
        this.userId = userId;
        this.categoryId = categoryId;
        this.commentsId = commentsId;
        this.labelsId = labelsId;
    }
}