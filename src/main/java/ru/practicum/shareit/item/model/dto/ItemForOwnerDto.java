package ru.practicum.shareit.item.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

@Data
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
