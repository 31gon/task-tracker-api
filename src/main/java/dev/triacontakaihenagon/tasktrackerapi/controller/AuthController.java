package dev.triacontakaihenagon.tasktrackerapi.controller;

import dev.triacontakaihenagon.jwtauth.dto.LoginRequest;
import dev.triacontakaihenagon.jwtauth.dto.LoginResponse;
import dev.triacontakaihenagon.jwtauth.dto.UserRequest;
import dev.triacontakaihenagon.jwtauth.dto.UserResponse;
import dev.triacontakaihenagon.jwtauth.entity.Role;
import dev.triacontakaihenagon.jwtauth.entity.User;
import dev.triacontakaihenagon.jwtauth.service.JwtService;
import dev.triacontakaihenagon.jwtauth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody @Valid UserRequest request) {
        request.setRole(Role.USER);
        User user = new User(request);
        return new UserResponse(userService.createUser(user));
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
        );
        String token = jwtService.generateToken(request.getUserName());
        return new LoginResponse(token);
    }
}