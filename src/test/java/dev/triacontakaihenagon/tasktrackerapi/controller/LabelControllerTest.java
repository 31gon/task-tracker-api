package dev.triacontakaihenagon.tasktrackerapi.controller;

import dev.triacontakaihenagon.tasktrackerapi.dto.LabelRequest;
import dev.triacontakaihenagon.tasktrackerapi.dto.LabelResponse;
import dev.triacontakaihenagon.tasktrackerapi.entity.Label;
import dev.triacontakaihenagon.tasktrackerapi.mapper.LabelMapper;
import dev.triacontakaihenagon.tasktrackerapi.service.LabelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LabelController.class)
class LabelControllerTest extends SecuredWebMvcTestBase{

    @Autowired MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean LabelService labelService;
    @MockitoBean LabelMapper labelMapper;

    @Test
    @WithMockUser
    void getLabels_returns200() throws Exception {
        when(labelService.getAllLabels()).thenReturn(java.util.List.of());
        when(labelMapper.toResponseList(java.util.List.of())).thenReturn(java.util.List.of());

        mockMvc.perform(get("/labels"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createLabel_returns200ForAdmin() throws Exception {
        LabelRequest request = new LabelRequest();
        request.setName("urgent");
        Label saved = new Label();
        LabelResponse response = new LabelResponse();
        response.setName("urgent");

        when(labelService.createLabel(any())).thenReturn(saved);
        when(labelMapper.toResponse(saved)).thenReturn(response);

        mockMvc.perform(post("/labels")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("urgent"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void createLabel_returns403ForNonAdmin() throws Exception {
        LabelRequest request = new LabelRequest();
        request.setName("urgent");

        mockMvc.perform(post("/labels")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteLabel_returns200ForAdmin() throws Exception {
        mockMvc.perform(delete("/labels/1").with(csrf()))
                .andExpect(status().isOk());
    }
}