package dev.triacontakaihenagon.tasktrackerapi.dto;

import dev.triacontakaihenagon.tasktrackerapi.entity.TaskPriority;
import dev.triacontakaihenagon.tasktrackerapi.entity.TaskStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record TaskFilter(
        TaskStatus status,
        TaskPriority priority,
        Long categoryId,
        String q,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdFrom,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdTo
) {}
