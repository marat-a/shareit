package ru.practicum.gateway.common.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String mes) {
        super(mes);
    }
}

