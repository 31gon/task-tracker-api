package dev.triacontakaihenagon.tasktrackerapi.controller;

import dev.triacontakaihenagon.tasktrackerapi.dto.LoginRequest;
import dev.triacontakaihenagon.tasktrackerapi.dto.UserRequest;
import dev.triacontakaihenagon.tasktrackerapi.dto.UserResponse;
import dev.triacontakaihenagon.tasktrackerapi.entity.Role;
import dev.triacontakaihenagon.tasktrackerapi.entity.User;
import dev.triacontakaihenagon.tasktrackerapi.mapper.UserMapper;
import dev.triacontakaihenagon.tasktrackerapi.security.JwtService;
import dev.triacontakaihenagon.tasktrackerapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends SecuredWebMvcTestBase{

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean UserService userService;
    @MockitoBean UserMapper userMapper;

    @Test
    void register_forcesUserRoleRegardlessOfRequestedRole() throws Exception {
        UserRequest request = new UserRequest();
        request.setUserName("bob");
        request.setPassword("plain");
        request.setRole(Role.ADMIN);

        when(userService.createUser(any())).thenReturn(new User());
        when(userMapper.toResponse(any())).thenReturn(new UserResponse());

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk());

        ArgumentCaptor<UserRequest> captor = org.mockito.ArgumentCaptor.forClass(UserRequest.class);
        verify(userService).createUser(captor.capture());
        assertEquals(Role.USER, captor.getValue().getRole());
    }

    @Test
    void login_returns200WithToken() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUserName("alice");
        request.setPassword("correct");

        when(jwtService.generateToken("alice")).thenReturn("fake-jwt-token");

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    void login_returns401OnBadCredentials() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUserName("alice");
        request.setPassword("wrong");

        doThrow(new BadCredentialsException("bad"))
                .when(authenticationManager).authenticate(any());

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }
}