package dev.triacontakaihenagon.tasktrackerapi.service;

import dev.triacontakaihenagon.tasktrackerapi.dto.LabelFilter;
import dev.triacontakaihenagon.tasktrackerapi.dto.LabelRequest;
import dev.triacontakaihenagon.tasktrackerapi.entity.Label;
import dev.triacontakaihenagon.tasktrackerapi.exception.LabelNotFoundException;
import dev.triacontakaihenagon.tasktrackerapi.repository.LabelRepository;
import dev.triacontakaihenagon.tasktrackerapi.repository.LabelSpecs;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {
    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    public Label createLabel(LabelRequest request) {
        Label category = new Label();
        category.setName(request.getName());
        return labelRepository.save(category);
    }

    public Label updateLabel(Long id, LabelRequest request) {
        Label category = labelRepository.findById(id)
                .orElseThrow(() -> new LabelNotFoundException("Label with " + id + " not found"));
        category.setName(request.getName());
        return labelRepository.save(category);
    }

    public void deleteLabel(Long id) {
        if (labelRepository.existsById(id)) labelRepository.deleteById(id);
        else throw new LabelNotFoundException("Label with " + id + " not found");
    }

    public List<Label> search(LabelFilter f) {
        Specification<Label> spec = Specification.allOf(LabelSpecs.nameContains(f.name()));
        return labelRepository.findAll(spec);
    }
}