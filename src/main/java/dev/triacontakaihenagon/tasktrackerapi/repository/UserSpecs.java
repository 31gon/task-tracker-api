package dev.triacontakaihenagon.tasktrackerapi.repository;

import dev.triacontakaihenagon.tasktrackerapi.entity.*;
import org.springframework.data.jpa.domain.Specification;

public final class UserSpecs {
    private UserSpecs() {}

    public static Specification<User> hasRole(Role s) {
        return (root, query, cb) -> s == null ? null : cb.equal(root.get("role"), s);
    }

    public static Specification<User> nameContains(String text) {
        return (root, query, cb) -> {
            if (text == null || text.isBlank()) return null;
            String escaped = text.toLowerCase()
                    .replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
            return cb.like(cb.lower(root.get("userName")), "%" + escaped + "%", '\\');
        };
    }
}
