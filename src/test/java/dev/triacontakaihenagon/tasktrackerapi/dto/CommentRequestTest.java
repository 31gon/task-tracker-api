package dev.triacontakaihenagon.tasktrackerapi.dto;

import org.junit.jupiter.api.Test;

import static dev.triacontakaihenagon.tasktrackerapi.dto.ValidationTestSupport.VALIDATOR;
import static org.assertj.core.api.Assertions.assertThat;

class CommentRequestTest {

    @Test
    void blankName_failsValidation() {
        CommentRequest request = new CommentRequest();
        request.setContent("");

        assertThat(VALIDATOR.validate(request)).isNotEmpty();
    }

    @Test
    void nonBlankName_passesValidation() {
        CommentRequest request = new CommentRequest();
        request.setContent("Work");

        assertThat(VALIDATOR.validate(request)).isEmpty();
    }
}
