package ru.practicum.server.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String mes) {
        super(mes);
    }
}

