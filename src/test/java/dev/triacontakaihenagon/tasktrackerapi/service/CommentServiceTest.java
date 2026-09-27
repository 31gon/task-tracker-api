package dev.triacontakaihenagon.tasktrackerapi.service;

import dev.triacontakaihenagon.tasktrackerapi.dto.CommentRequest;
import dev.triacontakaihenagon.tasktrackerapi.entity.Task;
import dev.triacontakaihenagon.tasktrackerapi.entity.User;
import dev.triacontakaihenagon.tasktrackerapi.exception.TaskNotFoundException;
import dev.triacontakaihenagon.tasktrackerapi.exception.UserNotFoundException;
import dev.triacontakaihenagon.tasktrackerapi.repository.CommentRepository;
import dev.triacontakaihenagon.tasktrackerapi.repository.TaskRepository;
import dev.triacontakaihenagon.tasktrackerapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock CommentRepository commentRepository;
    @Mock TaskRepository taskRepository;
    @Mock UserRepository userRepository;
    @InjectMocks CommentService commentService;

    @Test
    void addComment_savesWithTaskAndAuthor() {
        Task task = new Task(); task.setId(5L);
        User author = new User(); author.setUserName("alice");
        CommentRequest request = new CommentRequest();
        request.setContent("Looks good");

        when(taskRepository.findById(5L)).thenReturn(Optional.of(task));
        when(userRepository.findByUserName("alice")).thenReturn(Optional.of(author));
        when(commentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = commentService.addComment(5L, request, "alice");

        assertThat(result.getContent()).isEqualTo("Looks good");
        assertThat(result.getTask()).isEqualTo(task);
        assertThat(result.getAuthor()).isEqualTo(author);
    }

    @Test
    void addComment_throwsWhenTaskNotFound() {
        when(taskRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.addComment(404L, new CommentRequest(), "alice"))
                .isInstanceOf(TaskNotFoundException.class);

        verifyNoInteractions(userRepository, commentRepository);
    }

    @Test
    void addComment_throwsWhenAuthorNotFound() {
        Task task = new Task(); task.setId(5L);
        when(taskRepository.findById(5L)).thenReturn(Optional.of(task));
        when(userRepository.findByUserName("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.addComment(5L, new CommentRequest(), "ghost"))
                .isInstanceOf(UserNotFoundException.class);

        verifyNoInteractions(commentRepository);
    }
}