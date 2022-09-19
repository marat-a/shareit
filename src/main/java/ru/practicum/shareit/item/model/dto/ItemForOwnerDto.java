package ru.practicum.shareit.item.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemForOwnerDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private ItemRequest request;
    private List<CommentDto> comments;
}
