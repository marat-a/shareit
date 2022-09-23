package ru.practicum.gateway.common.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String mes) {
        super(mes);
    }
}

