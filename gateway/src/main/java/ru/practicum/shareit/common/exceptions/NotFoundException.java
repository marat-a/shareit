package ru.practicum.shareit.common.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException (String mes) {
        super(mes);
    }
}
