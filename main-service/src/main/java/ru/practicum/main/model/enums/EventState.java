package ru.practicum.main.model.enums;

import ru.practicum.main.exceptions.StateValidationException;

public enum EventState {
    PENDING,
    PUBLISHED,
    CANCELED;


    public static EventState getStateValue(String state) {
        try {
            return EventState.valueOf(state);
        } catch (Exception e) {
            throw new StateValidationException("Unknown state: " + state);
        }
    }
}