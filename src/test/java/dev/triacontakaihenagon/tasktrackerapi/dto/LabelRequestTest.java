package dev.triacontakaihenagon.tasktrackerapi.dto;

import org.junit.jupiter.api.Test;

import static dev.triacontakaihenagon.tasktrackerapi.dto.ValidationTestSupport.VALIDATOR;
import static org.assertj.core.api.Assertions.assertThat;

class LabelRequestTest {

    @Test
    void blankName_failsValidation() {
        LabelRequest request = new LabelRequest();
        request.setName("");

        assertThat(VALIDATOR.validate(request)).isNotEmpty();
    }

    @Test
    void nonBlankName_passesValidation() {
        LabelRequest request = new LabelRequest();
        request.setName("Work");

        assertThat(VALIDATOR.validate(request)).isEmpty();
    }
}

