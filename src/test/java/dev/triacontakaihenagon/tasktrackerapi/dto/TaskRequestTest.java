package dev.triacontakaihenagon.tasktrackerapi.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static dev.triacontakaihenagon.tasktrackerapi.dto.ValidationTestSupport.VALIDATOR;

class TaskRequestTest {

    @Test
    void blankTitle_failsValidation() {
        TaskRequest request = new TaskRequest();
        request.setTitle("");

        assertThat(VALIDATOR.validate(request)).isNotEmpty();
    }

    @Test
    void nonBlankTitle_passesValidation() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Write tests");

        assertThat(VALIDATOR.validate(request)).isEmpty();
    }
}