package ru.practicum.shareit.enums;

import ru.practicum.shareit.exceptions.StateException;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;
    public static State toState(String state) {
        try {
            return State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new StateException("Unknown state: " + state);
        }
    }
}

