package dev.triacontakaihenagon.tasktrackerapi.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static dev.triacontakaihenagon.tasktrackerapi.dto.ValidationTestSupport.VALIDATOR;

class LoginRequestTest {

    @Test
    void blankUserNameOrPassword_failsValidation() {
        LoginRequest request = new LoginRequest();
        request.setUserName("");
        request.setPassword("");

        assertThat(VALIDATOR.validate(request)).hasSize(2);
    }

    @Test
    void validRequest_passesValidation() {
        LoginRequest request = new LoginRequest();
        request.setUserName("alice");
        request.setPassword("secret");

        assertThat(VALIDATOR.validate(request)).isEmpty();
    }
}