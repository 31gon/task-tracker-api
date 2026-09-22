package dev.triacontakaihenagon.tasktrackerapi.service;

import dev.triacontakaihenagon.tasktrackerapi.dto.TaskFilter;
import dev.triacontakaihenagon.tasktrackerapi.dto.TaskRequest;
import dev.triacontakaihenagon.tasktrackerapi.entity.*;
import dev.triacontakaihenagon.tasktrackerapi.exception.CategoryNotFoundException;
import dev.triacontakaihenagon.tasktrackerapi.exception.TaskNotFoundException;
import dev.triacontakaihenagon.tasktrackerapi.exception.UserNotFoundException;
import dev.triacontakaihenagon.tasktrackerapi.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskService {
    private static final Set<String> SORTABLE = Set.of("id", "title", "status", "priority", "createdAt");
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LabelRepository labelRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, CategoryRepository categoryRepository, LabelRepository labelRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.labelRepository = labelRepository;
    }

    public List<Task> getAllTask() {
        return taskRepository.findAll();
    }
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }
    public Task createTask(TaskRequest request, String username) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setStatus(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO);
        task.setPriority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM);

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        task.setUser(user);

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category with id: " + request.getCategoryId() + " not found."));
            task.setCategory(category);
        }
        if (request.getLabelIds() != null) {
            task.setLabels(labelRepository.findAllById(request.getLabelIds()));
        }
        return taskRepository.save(task);
    }

    public Task getTaskById(Long id, String username, boolean isAdmin) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with " + id + " not found"));
        checkOwnership(task, username, isAdmin);
        return task;
    }

    public Task updateTask(Long id, TaskRequest request, String username, boolean isAdmin) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with " + id + " not found"));
        checkOwnership(existingTask, username, isAdmin);

        existingTask.setTitle(request.getTitle());
        existingTask.setStatus(request.getStatus());
        existingTask.setPriority(request.getPriority());
        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id, String username, boolean isAdmin) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with " + id + " not found"));
        checkOwnership(task, username, isAdmin);
        taskRepository.deleteById(id);
    }

    private void checkOwnership(Task task, String username, boolean isAdmin) {
        if (!isAdmin && !task.getUser().getUserName().equals(username)) {
            throw new AccessDeniedException("You do not own this task");
        }
    }

    public void deleteTask(Long id) {
        if (taskRepository.existsById(id)) taskRepository.deleteById(id);
        else throw new TaskNotFoundException("Task with " + id + " not found ");
    }

    public Page<Task> search(TaskFilter f, Pageable pageable) {
        Specification<Task> spec = Specification.allOf(
                TaskSpecs.hasStatus(f.status()),
                TaskSpecs.hasPriority(f.priority()),
                TaskSpecs.inCategory(f.categoryId()),
                TaskSpecs.titleContains(f.q()),
                TaskSpecs.createdFrom(f.createdFrom()),
                TaskSpecs.createdTo(f.createdTo()));
        return taskRepository.findAll(spec, safe(pageable));
    }


    private Pageable safe(Pageable p) {
        List<Sort.Order> orders = new ArrayList<>();
        for (Sort.Order o : p.getSort()) {
            if (!SORTABLE.contains(o.getProperty())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot sort by: " + o.getProperty());
            }
            orders.add(o);
        }
        if (orders.stream().noneMatch(o -> o.getProperty().equals("id"))) {
            orders.add(Sort.Order.asc("id"));
        }
        return PageRequest.of(p.getPageNumber(), p.getPageSize(), Sort.by(orders));
    }
}
