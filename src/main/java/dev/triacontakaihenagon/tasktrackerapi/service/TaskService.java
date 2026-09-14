package dev.triacontakaihenagon.tasktrackerapi.service;

import dev.triacontakaihenagon.tasktrackerapi.entity.Task;
import dev.triacontakaihenagon.tasktrackerapi.exception.TaskNotFoundException;
import dev.triacontakaihenagon.tasktrackerapi.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    public List<Task> getAllTask() {
        return taskRepository.findAll();
    }
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }
    public void deleteTask(Long id) {
        if (taskRepository.existsById(id)) taskRepository.deleteById(id);
        else throw new TaskNotFoundException("Task with " + id + " not found ");
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with "+ id +" not found "));
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDone(updatedTask.isDone());
        return taskRepository.save(existingTask);
    }
}
