package ru.practicum.shareit.booking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NewBookingDto {
    @NonNull
    private Long itemId;
    @NonNull
    private LocalDateTime start;
    @NonNull
    private LocalDateTime end;
}
