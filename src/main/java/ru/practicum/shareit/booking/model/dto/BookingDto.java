package ru.practicum.shareit.booking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.user.model.UserDto;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private UserDto booker;
    private long bookerId;
    private ItemDto item;
    private Status status;
}
