package dev.triacontakaihenagon.tasktrackerapi.controller;

import dev.triacontakaihenagon.tasktrackerapi.dto.UserRequest;
import dev.triacontakaihenagon.tasktrackerapi.dto.UserResponse;
import dev.triacontakaihenagon.tasktrackerapi.entity.Role;
import dev.triacontakaihenagon.tasktrackerapi.entity.User;
import dev.triacontakaihenagon.tasktrackerapi.mapper.UserMapper;
import dev.triacontakaihenagon.tasktrackerapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest extends SecuredWebMvcTestBase{

    @Autowired MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean UserService userService;
    @MockitoBean UserMapper userMapper;

    @Test
    @WithMockUser
    void getUser_returns200WhenFound() throws Exception {
        User user = new User();
        user.setId(1L);
        UserResponse response = new UserResponse(1L, "alice", Role.USER);

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("alice"));
    }

    @Test
    @WithMockUser
    void getUser_returns404WhenNotFound() throws Exception {
        when(userService.getUserById(404L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/404"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_returns200ForAdmin() throws Exception {
        mockMvc.perform(delete("/users/1").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteUser_returns403ForNonAdmin() throws Exception {
        mockMvc.perform(delete("/users/1").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUser_returns200ForAdmin() throws Exception {
        UserRequest request = new UserRequest();
        request.setUserName("alice2");
        request.setRole(Role.USER);

        User updated = new User();
        UserResponse response = new UserResponse(1L, "alice2", Role.USER);

        when(userService.updateUser(eq(1L), any())).thenReturn(updated);
        when(userMapper.toResponse(updated)).thenReturn(response);

        mockMvc.perform(put("/users/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("alice2"));
    }

    @Test
    @WithMockUser(username = "alice")
    void me_returns200() throws Exception {
        User user = new User();
        user.setUserName("alice");
        UserResponse response = new UserResponse(1L, "alice", Role.USER);

        when(userService.getCurrentUser("alice")).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("alice"));
    }

    @Test
    @WithMockUser(username = "ghost")
    void me_returns404WhenNotFound() throws Exception {
        when(userService.getCurrentUser("ghost")).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found: ghost"));
    }
}