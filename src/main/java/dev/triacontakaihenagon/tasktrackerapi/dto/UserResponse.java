package dev.triacontakaihenagon.tasktrackerapi.dto;

import dev.triacontakaihenagon.jwtauth.entity.Role;
import dev.triacontakaihenagon.jwtauth.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private Long id;
    private String userName;
    private Role role;

    public UserResponse(Long id, String userName, Role role) {
        this.id = id;
        this.userName = userName;
        this.role = role;
    }

    public UserResponse(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.role = user.getRole();
    }
}