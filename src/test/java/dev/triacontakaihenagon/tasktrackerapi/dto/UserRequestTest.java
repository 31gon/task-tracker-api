package dev.triacontakaihenagon.tasktrackerapi.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static dev.triacontakaihenagon.tasktrackerapi.dto.ValidationTestSupport.VALIDATOR;

class UserRequestTest {

    @Test
    void blankUserName_failsValidation() {
        UserRequest request = new UserRequest();
        request.setUserName("");

        assertThat(VALIDATOR.validate(request)).isNotEmpty();
    }

    @Test
    void nonBlankUserName_passesValidation() {
        UserRequest request = new UserRequest();
        request.setUserName("alice");

        assertThat(VALIDATOR.validate(request)).isEmpty();
    }
}