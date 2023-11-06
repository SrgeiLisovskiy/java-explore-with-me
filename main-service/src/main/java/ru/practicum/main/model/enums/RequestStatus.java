package ru.practicum.main.model.enums;

import ru.practicum.main.exceptions.StateValidationException;

public enum RequestStatus {
    PENDING,
    CONFIRMED,
    REJECTED,
    CANCELED;

    public static RequestStatus getStatusValue(String requestStatus) {
        try {
            return RequestStatus.valueOf(requestStatus);
        } catch (Exception e) {
            throw new StateValidationException("Unknown status: " + requestStatus);
        }
    }
}
