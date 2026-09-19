package dev.triacontakaihenagon.tasktrackerapi.service;

import dev.triacontakaihenagon.tasktrackerapi.dto.LabelRequest;
import dev.triacontakaihenagon.tasktrackerapi.entity.Label;
import dev.triacontakaihenagon.tasktrackerapi.exception.LabelNotFoundException;
import dev.triacontakaihenagon.tasktrackerapi.repository.LabelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {
    private final LabelRepository categoryRepository;

    public LabelService(LabelRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Label> getAllLabels() {
        return categoryRepository.findAll();
    }

    public Label createLabel(LabelRequest request) {
        Label category = new Label();
        category.setName(request.getName());
        return categoryRepository.save(category);
    }

    public Label updateLabel(Long id, LabelRequest request) {
        Label category = categoryRepository.findById(id)
                .orElseThrow(() -> new LabelNotFoundException("Label with " + id + " not found"));
        category.setName(request.getName());
        return categoryRepository.save(category);
    }

    public void deleteLabel(Long id) {
        if (categoryRepository.existsById(id)) categoryRepository.deleteById(id);
        else throw new LabelNotFoundException("Label with " + id + " not found");
    }
}