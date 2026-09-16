package dev.triacontakaihenagon.tasktrackerapi.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) { super(message);}
}
