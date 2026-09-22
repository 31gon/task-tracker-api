package dev.triacontakaihenagon.tasktrackerapi.controller;

import dev.triacontakaihenagon.tasktrackerapi.dto.LabelFilter;
import dev.triacontakaihenagon.tasktrackerapi.dto.LabelRequest;
import dev.triacontakaihenagon.tasktrackerapi.dto.LabelResponse;
import dev.triacontakaihenagon.tasktrackerapi.mapper.LabelMapper;
import dev.triacontakaihenagon.tasktrackerapi.service.LabelService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/labels")
public class LabelController {
    private final LabelService labelService;
    private final LabelMapper labelMapper;

    public LabelController(LabelService labelService, LabelMapper labelMapper) {
        this.labelService = labelService;
        this.labelMapper = labelMapper;
    }

    @GetMapping
    public List<LabelResponse> getLabels() {
        return labelMapper.toResponseList(labelService.getAllLabels());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public LabelResponse createLabel(@RequestBody @Valid LabelRequest request) {
        return labelMapper.toResponse(labelService.createLabel(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public LabelResponse updateLabel(@PathVariable Long id, @RequestBody @Valid LabelRequest request) {
        return labelMapper.toResponse(labelService.updateLabel(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteLabel(@PathVariable Long id) {
        labelService.deleteLabel(id);
    }

    @GetMapping("/search")
    public List<LabelResponse> search(LabelFilter filter) {
        return labelMapper.toResponseList(labelService.search(filter));
    }
}