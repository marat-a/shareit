package yandex.practicum.shareit.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException (String message) {
        super(message);
    }
}
