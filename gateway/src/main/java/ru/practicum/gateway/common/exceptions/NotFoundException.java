package ru.practicum.gateway.common.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException (String mes) {
        super(mes);
    }
}
