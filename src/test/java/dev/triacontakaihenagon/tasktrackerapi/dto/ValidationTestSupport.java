package dev.triacontakaihenagon.tasktrackerapi.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

class ValidationTestSupport {
    static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
}