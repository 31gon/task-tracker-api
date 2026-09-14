package dev.triacontakaihenagon.tasktrackerapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {
    @NotBlank
    private String title;
    private boolean done;
}