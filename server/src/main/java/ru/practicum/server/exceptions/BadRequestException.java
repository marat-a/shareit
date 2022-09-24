package ru.practicum.server.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String mes) {
        super(mes);
    }
}

