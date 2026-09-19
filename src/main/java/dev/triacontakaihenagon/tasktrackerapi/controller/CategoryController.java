package dev.triacontakaihenagon.tasktrackerapi.controller;

import dev.triacontakaihenagon.tasktrackerapi.dto.CategoryRequest;
import dev.triacontakaihenagon.tasktrackerapi.dto.CategoryResponse;
import dev.triacontakaihenagon.tasktrackerapi.mapper.CategoryMapper;
import dev.triacontakaihenagon.tasktrackerapi.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    public List<CategoryResponse> getCategories() {
        return categoryMapper.toResponseList(categoryService.getAllCategories());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CategoryResponse createCategory(@RequestBody @Valid CategoryRequest request) {
        return categoryMapper.toResponse(categoryService.createCategory(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CategoryResponse updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryRequest request) {
        return categoryMapper.toResponse(categoryService.updateCategory(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}