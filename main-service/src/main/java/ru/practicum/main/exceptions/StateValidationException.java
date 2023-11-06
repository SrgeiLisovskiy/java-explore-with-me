package ru.practicum.main.exceptions;

public class StateValidationException extends RuntimeException {
    public StateValidationException(String message) {
        super(message);
    }
}
