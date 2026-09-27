package dev.triacontakaihenagon.tasktrackerapi.controller;

import dev.triacontakaihenagon.tasktrackerapi.dto.CategoryRequest;
import dev.triacontakaihenagon.tasktrackerapi.dto.CategoryResponse;
import dev.triacontakaihenagon.tasktrackerapi.entity.Category;
import dev.triacontakaihenagon.tasktrackerapi.mapper.CategoryMapper;
import dev.triacontakaihenagon.tasktrackerapi.service.CategoryService;
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

@WebMvcTest(CategoryController.class)
class CategoryControllerTest extends SecuredWebMvcTestBase{

    @Autowired MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean CategoryService categoryService;
    @MockitoBean CategoryMapper categoryMapper;

    @Test
    @WithMockUser
    void getCategories_returns200() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(java.util.List.of());
        when(categoryMapper.toResponseList(java.util.List.of())).thenReturn(java.util.List.of());

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCategory_returns200ForAdmin() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Work");
        Category saved = new Category();
        CategoryResponse response = new CategoryResponse();
        response.setName("Work");

        when(categoryService.createCategory(any())).thenReturn(saved);
        when(categoryMapper.toResponse(saved)).thenReturn(response);

        mockMvc.perform(post("/categories")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Work"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void createCategory_returns403ForNonAdmin() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Work");

        mockMvc.perform(post("/categories")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCategory_returns200ForAdmin() throws Exception {
        mockMvc.perform(delete("/categories/1").with(csrf()))
                .andExpect(status().isOk());
    }
}