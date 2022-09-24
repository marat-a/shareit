package ru.practicum.server.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException (String mes) {
        super(mes);
    }
}
