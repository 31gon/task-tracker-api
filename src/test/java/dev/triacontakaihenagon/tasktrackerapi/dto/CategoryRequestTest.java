package dev.triacontakaihenagon.tasktrackerapi.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static dev.triacontakaihenagon.tasktrackerapi.dto.ValidationTestSupport.VALIDATOR;

class CategoryRequestTest {

    @Test
    void blankName_failsValidation() {
        CategoryRequest request = new CategoryRequest();
        request.setName("");

        assertThat(VALIDATOR.validate(request)).isNotEmpty();
    }

    @Test
    void nonBlankName_passesValidation() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Work");

        assertThat(VALIDATOR.validate(request)).isEmpty();
    }
}


