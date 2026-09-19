package dev.triacontakaihenagon.tasktrackerapi.controller;

import dev.triacontakaihenagon.tasktrackerapi.dto.TaskRequest;
import dev.triacontakaihenagon.tasktrackerapi.dto.TaskResponse;
import dev.triacontakaihenagon.tasktrackerapi.mapper.TaskMapper;
import dev.triacontakaihenagon.tasktrackerapi.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping
    public List<TaskResponse> getTasks() {
        return taskMapper.toResponseList(taskService.getAllTask());
    }

    @GetMapping("/{id}")
    public TaskResponse getTask(@PathVariable Long id, Authentication auth) {
        return taskMapper.toResponse(taskService.getTaskById(id, auth.getName(), isAdmin(auth)));
    }

    @PostMapping
    public TaskResponse postTasks(@RequestBody @Valid TaskRequest request, Authentication auth) {
        return taskMapper.toResponse(taskService.createTask(request, auth.getName()));
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id, @RequestBody @Valid TaskRequest request, Authentication auth) {
        return taskMapper.toResponse(taskService.updateTask(id, request, auth.getName(), isAdmin(auth)));
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id, Authentication auth) {
        taskService.deleteTask(id, auth.getName(), isAdmin(auth));
    }

    private boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
