package ru.practicum.shareit.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String mes) {
        super(mes);
    }
}

