package dev.triacontakaihenagon.tasktrackerapi.repository;

import dev.triacontakaihenagon.tasktrackerapi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> { }
