package dev.triacontakaihenagon.tasktrackerapi.exception;


public class LabelNotFoundException extends RuntimeException {
    public LabelNotFoundException(String message) {
        super(message);
    }
}

