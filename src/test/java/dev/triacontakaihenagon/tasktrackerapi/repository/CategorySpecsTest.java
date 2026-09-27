package dev.triacontakaihenagon.tasktrackerapi.repository;

import dev.triacontakaihenagon.tasktrackerapi.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CategorySpecsTest {

    @Autowired CategoryRepository categoryRepository;

    private void save(String name) {
        Category c = new Category();
        c.setName(name);
        categoryRepository.save(c);
    }

    @Test
    void nameContains_matchesCaseInsensitively() {
        save("Work");
        save("Personal");

        List<Category> result = categoryRepository.findAll(
                Specification.allOf(CategorySpecs.nameContains("wor")));

        assertThat(result).extracting(Category::getName).containsExactly("Work");
    }

    @Test
    void nameContains_treatsPercentAsLiteralCharacter() {
        save("100%done");
        save("100xdone");

        List<Category> result = categoryRepository.findAll(
                Specification.allOf(CategorySpecs.nameContains("100%")));

        assertThat(result).extracting(Category::getName).containsExactly("100%done");
    }

    @Test
    void nameContains_treatsUnderscoreAsLiteralCharacter() {
        save("a_b");
        save("aXb");

        List<Category> result = categoryRepository.findAll(
                Specification.allOf(CategorySpecs.nameContains("a_b")));

        assertThat(result).extracting(Category::getName).containsExactly("a_b");
    }

    @Test
    void nameContains_blankOrNull_returnsEverything() {
        save("Work");
        save("Personal");

        List<Category> blank = categoryRepository.findAll(
                Specification.allOf(CategorySpecs.nameContains("  ")));
        List<Category> nul = categoryRepository.findAll(
                Specification.allOf(CategorySpecs.nameContains(null)));

        assertThat(blank).hasSize(2);
        assertThat(nul).hasSize(2);
    }
}
