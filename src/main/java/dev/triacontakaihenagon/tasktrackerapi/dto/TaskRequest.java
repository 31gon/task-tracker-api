package dev.triacontakaihenagon.tasktrackerapi.dto;

import dev.triacontakaihenagon.tasktrackerapi.entity.TaskPriority;
import dev.triacontakaihenagon.tasktrackerapi.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class TaskRequest {
    @NotBlank
    private String title;
    private TaskStatus status;
    private TaskPriority priority;
    private Long categoryId;
    private List<Long> labelIds;
}