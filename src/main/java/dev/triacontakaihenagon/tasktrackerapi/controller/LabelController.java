package dev.triacontakaihenagon.tasktrackerapi.controller;

import dev.triacontakaihenagon.tasktrackerapi.dto.LabelRequest;
import dev.triacontakaihenagon.tasktrackerapi.dto.LabelResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class LabelController {
    private final LabelService labelService;
    private final LabelMapper labelMapper;

    public LabelController(LabelService categoryService, LabelMapper categoryMapper) {
        this.labelService = categoryService;
        this.labelMapper = categoryMapper;
    }

    @GetMapping
    public List<LabelResponse> getCategories() {
        return labelMapper.toResponseList(labelService.getAllCategories());
    }

    @PostMapping
    public LabelResponse createLabel(@RequestBody @Valid LabelRequest request) {
        return labelMapper.toResponse(labelService.createLabel(request));
    }

    @PutMapping("/{id}")
    public LabelResponse updateLabel(@PathVariable Long id, @RequestBody @Valid LabelRequest request) {
        return labelMapper.toResponse(labelService.updateLabel(id, request));
    }

    @DeleteMapping("/{id}")
    public void deleteLabel(@PathVariable Long id) {
        labelService.deleteLabel(id);
    }
}