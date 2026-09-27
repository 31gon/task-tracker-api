package dev.triacontakaihenagon.tasktrackerapi.repository;

import dev.triacontakaihenagon.tasktrackerapi.entity.Label;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LabelSpecsTest {

    @Autowired
    LabelRepository labelRepository;

    private void save(String name) {
        Label c = new Label();
        c.setName(name);
        labelRepository.save(c);
    }

    @Test
    void nameContains_matchesCaseInsensitively() {
        save("Work");
        save("Personal");

        List<Label> result = labelRepository.findAll(
                Specification.allOf(LabelSpecs.nameContains("wor")));

        assertThat(result).extracting(Label::getName).containsExactly("Work");
    }

    @Test
    void nameContains_treatsPercentAsLiteralCharacter() {
        save("100%done");
        save("100xdone");

        List<Label> result = labelRepository.findAll(
                Specification.allOf(LabelSpecs.nameContains("100%")));

        assertThat(result).extracting(Label::getName).containsExactly("100%done");
    }

    @Test
    void nameContains_treatsUnderscoreAsLiteralCharacter() {
        save("a_b");
        save("aXb");

        List<Label> result = labelRepository.findAll(
                Specification.allOf(LabelSpecs.nameContains("a_b")));

        assertThat(result).extracting(Label::getName).containsExactly("a_b");
    }

    @Test
    void nameContains_blankOrNull_returnsEverything() {
        save("Work");
        save("Personal");

        List<Label> blank = labelRepository.findAll(
                Specification.allOf(LabelSpecs.nameContains("  ")));
        List<Label> nul = labelRepository.findAll(
                Specification.allOf(LabelSpecs.nameContains(null)));

        assertThat(blank).hasSize(2);
        assertThat(nul).hasSize(2);
    }
}
