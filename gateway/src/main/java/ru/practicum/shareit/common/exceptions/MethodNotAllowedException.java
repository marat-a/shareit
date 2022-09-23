package ru.practicum.shareit.common.exceptions;

public class MethodNotAllowedException extends RuntimeException {
    public MethodNotAllowedException(String mes) {
        super(mes);
    }
}
