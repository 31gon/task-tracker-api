package dev.triacontakaihenagon.tasktrackerapi.controller;

import dev.triacontakaihenagon.tasktrackerapi.dto.TaskRequest;
import dev.triacontakaihenagon.tasktrackerapi.dto.TaskResponse;
import dev.triacontakaihenagon.tasktrackerapi.service.TaskService;
import dev.triacontakaihenagon.tasktrackerapi.entity.Task;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponse> getTasks() {
        return taskService.getAllTask().stream()
                .map(TaskResponse::new)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(TaskResponse::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TaskResponse postTasks(@RequestBody @Valid TaskRequest taskRequest) {
        Task task = new Task(taskRequest);
        return new TaskResponse(taskService.createTask(task));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id,@RequestBody @Valid TaskRequest taskRequest) {
        Task task = new Task(taskRequest);
        return new TaskResponse (taskService.updateTask(id, task));
    }
}
