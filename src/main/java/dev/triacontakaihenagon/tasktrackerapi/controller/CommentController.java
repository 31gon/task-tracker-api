package dev.triacontakaihenagon.tasktrackerapi.controller;

import dev.triacontakaihenagon.tasktrackerapi.dto.CommentRequest;
import dev.triacontakaihenagon.tasktrackerapi.dto.CommentResponse;
import dev.triacontakaihenagon.tasktrackerapi.mapper.CommentMapper;
import dev.triacontakaihenagon.tasktrackerapi.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks/{taskId}/comments")
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    public CommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @PostMapping
    public CommentResponse addComment(@PathVariable Long taskId, @RequestBody @Valid CommentRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return commentMapper.toResponse(commentService.addComment(taskId, request, username));
    }

    @GetMapping
    public List<CommentResponse> getComments(@PathVariable Long taskId) {
        return commentMapper.toResponseList(commentService.getCommentsForTask(taskId));
    }
}