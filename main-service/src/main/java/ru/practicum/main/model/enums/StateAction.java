package ru.practicum.main.model.enums;

import ru.practicum.main.exceptions.StateValidationException;

public enum StateAction {
    PUBLISH_EVENT,
    REJECT_EVENT;

    public static StateAction getStatusValue(String stateAction) {
        try {
            return StateAction.valueOf(stateAction);
        } catch (Exception e) {
            throw new StateValidationException("Unknown state: " + stateAction);
        }
    }
}
