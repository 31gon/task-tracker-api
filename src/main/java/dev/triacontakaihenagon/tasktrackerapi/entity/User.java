package dev.triacontakaihenagon.tasktrackerapi.entity;

import dev.triacontakaihenagon.jwtauth.dto.UserRequest;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    public User() {}

    public User(@Valid UserRequest userRequest) {
        this.userName = userRequest.getUserName();
        this.role = (userRequest.getRole() != null) ? userRequest.getRole() : Role.USER;
        this.password = userRequest.getPassword();
    }
    @Id
    @GeneratedValue
    private Long id;
    private String userName;
    private String password;//Hash later I guess
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks = new ArrayList<>();
}
