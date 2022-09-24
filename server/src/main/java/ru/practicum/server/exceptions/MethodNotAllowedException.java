package ru.practicum.server.exceptions;

public class MethodNotAllowedException extends RuntimeException {
    public MethodNotAllowedException(String mes) {
        super(mes);
    }
}
