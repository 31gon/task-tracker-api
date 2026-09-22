package dev.triacontakaihenagon.tasktrackerapi.repository;

import dev.triacontakaihenagon.tasktrackerapi.entity.Label;
import org.springframework.data.jpa.domain.Specification;

public final class LabelSpecs {
    private LabelSpecs() {}

    public static Specification<Label> nameContains(String text) {
        return (root, query, cb) -> {
            if (text == null || text.isBlank()) return null;
            String escaped = text.toLowerCase()
                    .replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
            return cb.like(cb.lower(root.get("name")), "%" + escaped + "%", '\\');
        };
    }
}