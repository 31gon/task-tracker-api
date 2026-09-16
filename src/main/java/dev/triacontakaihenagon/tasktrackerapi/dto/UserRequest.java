package dev.triacontakaihenagon.tasktrackerapi.dto;

import dev.triacontakaihenagon.jwtauth.entity.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    @NotBlank
    private String userName;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String password;
}