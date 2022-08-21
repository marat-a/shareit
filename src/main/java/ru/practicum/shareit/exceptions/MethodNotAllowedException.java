package ru.practicum.shareit.exceptions;

public class MethodNotAllowedException extends RuntimeException {
    public MethodNotAllowedException(String mes) {
        super(mes);
    }
}
