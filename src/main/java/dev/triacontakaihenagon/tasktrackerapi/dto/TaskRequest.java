package dev.triacontakaihenagon.tasktrackerapi.dto;

import dev.triacontakaihenagon.tasktrackerapi.entity.TaskPriority;
import dev.triacontakaihenagon.tasktrackerapi.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {
    @NotBlank
    private String title;
    private TaskStatus status;
    private Long userId;
    private TaskPriority priority;
}