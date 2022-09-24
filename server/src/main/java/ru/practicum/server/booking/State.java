package ru.practicum.server.booking;

import ru.practicum.server.exceptions.StateException;

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
