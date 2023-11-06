package ru.practicum.main.model.enums;

import ru.practicum.main.exceptions.StateValidationException;

public enum EventUserState {
    SEND_TO_REVIEW,
    CANCEL_REVIEW;

    public static EventUserState getStateValue(String state) {
        try {
            return EventUserState.valueOf(state);
        } catch (Exception e) {
            throw new StateValidationException("Unknown state: " + state);
        }
    }
}
