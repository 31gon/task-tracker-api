package dev.triacontakaihenagon.tasktrackerapi.dto;

import dev.triacontakaihenagon.tasktrackerapi.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class UserResponse {

    private Long id;
    private String userName;
    private Role role;
    private List<Long> taskIds;

    public UserResponse() {}

    public UserResponse(Long id, String userName, Role role) {
        this.id = id;
        this.userName = userName;
        this.role = role;
    }
}