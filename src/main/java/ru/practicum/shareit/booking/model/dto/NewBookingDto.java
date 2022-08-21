package ru.practicum.shareit.booking.model.dto;

import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class NewBookingDto {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
