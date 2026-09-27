package dev.triacontakaihenagon.tasktrackerapi.controller;

import dev.triacontakaihenagon.tasktrackerapi.dto.CommentRequest;
import dev.triacontakaihenagon.tasktrackerapi.dto.CommentResponse;
import dev.triacontakaihenagon.tasktrackerapi.entity.Comment;
import dev.triacontakaihenagon.tasktrackerapi.mapper.CommentMapper;
import dev.triacontakaihenagon.tasktrackerapi.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
class CommentControllerTest extends SecuredWebMvcTestBase {

    @Autowired MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean CommentService commentService;
    @MockitoBean CommentMapper commentMapper;

    @Test
    @WithMockUser(username = "alice")
    void addComment_returns200() throws Exception {
        CommentRequest request = new CommentRequest();
        request.setContent("nice work");

        Comment saved = new Comment();
        CommentResponse response = new CommentResponse();
        response.setContent("nice work");

        when(commentService.addComment(eq(5L), any(), eq("alice"))).thenReturn(saved);
        when(commentMapper.toResponse(saved)).thenReturn(response);

        mockMvc.perform(post("/tasks/5/comments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("nice work"));
    }

    @Test
    @WithMockUser
    void addComment_returns400WhenContentBlank() throws Exception {
        CommentRequest request = new CommentRequest();
        request.setContent("");

        mockMvc.perform(post("/tasks/5/comments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void getComments_returns200() throws Exception {
        when(commentService.getCommentsForTask(5L)).thenReturn(java.util.List.of());
        when(commentMapper.toResponseList(java.util.List.of())).thenReturn(java.util.List.of());

        mockMvc.perform(get("/tasks/5/comments"))
                .andExpect(status().isOk());
    }
}