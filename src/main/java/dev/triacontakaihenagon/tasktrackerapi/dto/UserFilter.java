package dev.triacontakaihenagon.tasktrackerapi.dto;

import dev.triacontakaihenagon.tasktrackerapi.entity.Role;

public record UserFilter(
        Role role,
        String name
) {}