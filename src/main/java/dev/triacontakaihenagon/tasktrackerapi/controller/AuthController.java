package dev.triacontakaihenagon.tasktrackerapi.controller;

import dev.triacontakaihenagon.tasktrackerapi.dto.LoginRequest;
import dev.triacontakaihenagon.tasktrackerapi.dto.LoginResponse;
import dev.triacontakaihenagon.tasktrackerapi.dto.UserRequest;
import dev.triacontakaihenagon.tasktrackerapi.dto.UserResponse;
import dev.triacontakaihenagon.tasktrackerapi.entity.Role;
import dev.triacontakaihenagon.tasktrackerapi.mapper.UserMapper;
import dev.triacontakaihenagon.tasktrackerapi.security.JwtService;
import dev.triacontakaihenagon.tasktrackerapi.service.UserService;
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
    private final UserMapper userMapper;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody @Valid UserRequest request) {
        request.setRole(Role.USER);
        return userMapper.toResponse(userService.createUser(request));
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