package dev.triacontakaihenagon.tasktrackerapi.controller;

import dev.triacontakaihenagon.tasktrackerapi.dto.TaskRequest;
import dev.triacontakaihenagon.tasktrackerapi.dto.TaskResponse;
import dev.triacontakaihenagon.tasktrackerapi.entity.Task;
import dev.triacontakaihenagon.tasktrackerapi.exception.TaskNotFoundException;
import dev.triacontakaihenagon.tasktrackerapi.mapper.TaskMapper;
import dev.triacontakaihenagon.tasktrackerapi.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest extends SecuredWebMvcTestBase {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean TaskService taskService;
    @MockitoBean TaskMapper taskMapper;

    @Test
    @WithMockUser(username = "alice")
    void getTask_returns200WithBody() throws Exception {
        Task task = new Task();
        task.setId(1L);
        TaskResponse response = new TaskResponse();
        response.setId(1L);
        response.setTitle("Write tests");

        when(taskService.getTaskById(eq(1L), eq("alice"), eq(false))).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(response);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Write tests"));
    }

    @Test
    @WithMockUser(username = "alice")
    void getTask_returns404WhenNotFound() throws Exception {
        when(taskService.getTaskById(eq(99L), eq("alice"), eq(false)))
                .thenThrow(new TaskNotFoundException("Task with 99 not found"));

        mockMvc.perform(get("/tasks/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task with 99 not found"));
    }

    @Test
    @WithMockUser(username = "bob")
    void getTask_returns403WhenAccessDenied() throws Exception {
        when(taskService.getTaskById(eq(1L), eq("bob"), eq(false)))
                .thenThrow(new AccessDeniedException("Not your task"));

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "alice", roles = "ADMIN")
    void getTask_passesAdminFlagWhenRoleAdmin() throws Exception {
        Task task = new Task();
        when(taskService.getTaskById(eq(1L), eq("alice"), eq(true))).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(new TaskResponse());

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "alice")
    void postTasks_returns200WhenValid() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("New task");

        Task saved = new Task();
        TaskResponse response = new TaskResponse();
        response.setTitle("New task");

        when(taskService.createTask(any(TaskRequest.class), eq("alice"))).thenReturn(saved);
        when(taskMapper.toResponse(saved)).thenReturn(response);

        mockMvc.perform(post("/tasks")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New task"));
    }

    @Test
    @WithMockUser(username = "alice")
    void postTasks_returns400WhenTitleBlank() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("");

        mockMvc.perform(post("/tasks")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "alice")
    void deleteTask_returns200() throws Exception {
        mockMvc.perform(delete("/tasks/1").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void getTask_returns401WhenUnauthenticated() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "alice")
    void getOverdueTasks_default_isGlobal() throws Exception {
        when(taskService.getOverdueTasks(null)).thenReturn(List.of());
        when(taskMapper.toResponseList(List.of())).thenReturn(List.of());

        mockMvc.perform(get("/tasks/overdue"))
                .andExpect(status().isOk());

        verify(taskService).getOverdueTasks(null);
    }

    @Test
    @WithMockUser(username = "alice")
    void getOverdueTasks_mineTrue_scopesToCurrentUser() throws Exception {
        when(taskService.getOverdueTasks("alice")).thenReturn(List.of());
        when(taskMapper.toResponseList(List.of())).thenReturn(List.of());

        mockMvc.perform(get("/tasks/overdue?mine=true"))
                .andExpect(status().isOk());

        verify(taskService).getOverdueTasks("alice");
    }
}