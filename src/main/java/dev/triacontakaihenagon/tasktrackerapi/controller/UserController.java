package dev.triacontakaihenagon.tasktrackerapi.controller;

import dev.triacontakaihenagon.jwtauth.dto.UserRequest;
import dev.triacontakaihenagon.jwtauth.dto.UserResponse;
import dev.triacontakaihenagon.jwtauth.entity.User;
import dev.triacontakaihenagon.jwtauth.exception.UserNotFoundException;
import dev.triacontakaihenagon.jwtauth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getUsers() {
        return userService.getAllUser().stream()
                .map(UserResponse::new)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(UserResponse::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody @Valid UserRequest userRequest) {
        User user = new User(userRequest);
        return new UserResponse (userService.updateUser(id, user));
    }

    @GetMapping("/me")
    public UserResponse me() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getCurrentUser(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        return new UserResponse(user);
    }
}
