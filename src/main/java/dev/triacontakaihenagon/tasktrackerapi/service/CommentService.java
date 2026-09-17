package dev.triacontakaihenagon.tasktrackerapi.service;

import dev.triacontakaihenagon.tasktrackerapi.dto.CommentRequest;
import dev.triacontakaihenagon.tasktrackerapi.entity.Comment;
import dev.triacontakaihenagon.tasktrackerapi.entity.Task;
import dev.triacontakaihenagon.tasktrackerapi.entity.User;
import dev.triacontakaihenagon.tasktrackerapi.exception.TaskNotFoundException;
import dev.triacontakaihenagon.tasktrackerapi.exception.UserNotFoundException;
import dev.triacontakaihenagon.tasktrackerapi.repository.CommentRepository;
import dev.triacontakaihenagon.tasktrackerapi.repository.TaskRepository;
import dev.triacontakaihenagon.tasktrackerapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Comment addComment(Long taskId, CommentRequest request, String authorUsername) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task with " + taskId + " not found"));
        User author = userRepository.findByUserName(authorUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + authorUsername));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setTask(task);
        comment.setAuthor(author);
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsForTask(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }
}