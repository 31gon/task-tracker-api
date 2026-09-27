package dev.triacontakaihenagon.tasktrackerapi.service;

import dev.triacontakaihenagon.tasktrackerapi.dto.TaskRequest;
import dev.triacontakaihenagon.tasktrackerapi.entity.*;
import dev.triacontakaihenagon.tasktrackerapi.exception.CategoryNotFoundException;
import dev.triacontakaihenagon.tasktrackerapi.exception.TaskNotFoundException;
import dev.triacontakaihenagon.tasktrackerapi.exception.UserNotFoundException;
import dev.triacontakaihenagon.tasktrackerapi.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock TaskRepository taskRepository;
    @Mock UserRepository userRepository;
    @Mock CategoryRepository categoryRepository;
    @Mock LabelRepository labelRepository;

    @InjectMocks TaskService taskService;

    private User owner;
    private Task task;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1L);
        owner.setUserName("alice");

        task = new Task();
        task.setId(10L);
        task.setUser(owner);
    }

    @Test
    void createTask_savesWithDefaultsAndOwner() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Write tests");

        when(userRepository.findByUserName("alice")).thenReturn(Optional.of(owner));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        Task result = taskService.createTask(request, "alice");

        assertThat(result.getTitle()).isEqualTo("Write tests");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(result.getPriority()).isEqualTo(TaskPriority.MEDIUM);
        assertThat(result.getUser()).isEqualTo(owner);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void createTask_throwsWhenUserNotFound() {
        TaskRequest request = new TaskRequest();
        when(userRepository.findByUserName("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.createTask(request, "ghost"))
                .isInstanceOf(UserNotFoundException.class);

        verifyNoInteractions(taskRepository);
    }

    @Test
    void createTask_throwsWhenCategoryNotFound() {
        TaskRequest request = new TaskRequest();
        request.setCategoryId(99L);

        when(userRepository.findByUserName("alice")).thenReturn(Optional.of(owner));
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.createTask(request, "alice"))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    void createTask_attachesLabelsWhenProvided() {
        TaskRequest request = new TaskRequest();
        request.setLabelIds(List.of(1L, 2L));
        Label l1 = new Label(); l1.setId(1L);
        Label l2 = new Label(); l2.setId(2L);

        when(userRepository.findByUserName("alice")).thenReturn(Optional.of(owner));
        when(labelRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(l1, l2));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        Task result = taskService.createTask(request, "alice");

        assertThat(result.getLabels()).containsExactly(l1, l2);
    }

    @Test
    void getTaskById_returnsTaskWhenOwner() {
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(10L, "alice", false);

        assertThat(result).isEqualTo(task);
    }

    @Test
    void getTaskById_returnsTaskWhenAdminNotOwner() {
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(10L, "someoneElse", true);

        assertThat(result).isEqualTo(task);
    }

    @Test
    void getTaskById_throwsWhenNotOwnerAndNotAdmin() {
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> taskService.getTaskById(10L, "bob", false))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void getTaskById_throwsWhenTaskNotFound() {
        when(taskRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(404L, "alice", false))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void updateTask_updatesFieldsWhenOwner() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Updated");
        request.setStatus(TaskStatus.DONE);
        request.setPriority(TaskPriority.HIGH);

        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.updateTask(10L, request, "alice", false);

        assertThat(result.getTitle()).isEqualTo("Updated");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.DONE);
        assertThat(result.getPriority()).isEqualTo(TaskPriority.HIGH);
    }

    @Test
    void updateTask_throwsWhenNotOwner() {
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> taskService.updateTask(10L, new TaskRequest(), "bob", false))
                .isInstanceOf(AccessDeniedException.class);

        verify(taskRepository, never()).save(any());
    }

    @Test
    void deleteTask_deletesWhenOwner() {
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        taskService.deleteTask(10L, "alice", false);

        verify(taskRepository).deleteById(10L);
    }

    @Test
    void deleteTask_throwsWhenNotOwner() {
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> taskService.deleteTask(10L, "bob", false))
                .isInstanceOf(AccessDeniedException.class);

        verify(taskRepository, never()).deleteById(any());
    }

    @Test
    void deleteTaskById_deletesWhenExists() {
        when(taskRepository.existsById(10L)).thenReturn(true);

        taskService.deleteTask(10L);

        verify(taskRepository).deleteById(10L);
    }

    @Test
    void deleteTaskById_throwsWhenNotFound() {
        when(taskRepository.existsById(404L)).thenReturn(false);

        assertThatThrownBy(() -> taskService.deleteTask(404L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void getOverdueTasks_returnsTasksFromRepository() {
        Task overdueTask = new Task();
        overdueTask.setDueDate(LocalDateTime.now().minusDays(1));
        overdueTask.setStatus(TaskStatus.TODO);

        when(taskRepository.findByDueDateBeforeAndStatusNot(any(LocalDateTime.class), eq(TaskStatus.DONE)))
                .thenReturn(List.of(overdueTask));

        List<Task> result = taskService.getOverdueTasks();

        assertThat(result).containsExactly(overdueTask);
    }

    @Test
    void getOverdueTasks_noneOverdue_returnsEmptyList() {
        when(taskRepository.findByDueDateBeforeAndStatusNot(any(LocalDateTime.class), eq(TaskStatus.DONE)))
                .thenReturn(List.of());

        assertThat(taskService.getOverdueTasks()).isEmpty();
    }
}