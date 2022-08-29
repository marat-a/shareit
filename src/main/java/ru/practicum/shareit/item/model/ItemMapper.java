package ru.practicum.shareit.item.model;

import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.item.model.dto.CommentDto;
import ru.practicum.shareit.item.model.dto.CommentMapper;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.ItemForOwnerDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {


    public static ItemDto toItemDto(Item item, List<CommentDto> comments) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest(),
                comments
        );
    }
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest(),
                new ArrayList<>()
        );
    }

    public static ItemForOwnerDto toItemForOwnerDto(Item item, BookingDto lastBooking, BookingDto nextBooking, List<CommentDto> comments) {
        return new ItemForOwnerDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                item.getRequest(),
                comments
        );
    }


    public static Item toItem(ItemDto itemDto, User owner) {
        return new Item(
                itemDto.getId(),
                owner,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getRequest()
        );
    }

    public static List<ItemForOwnerDto> toItemForOwnerDtoList(List<Item> itemList, ItemService itemService) {
        return itemList.stream()
                .map((Item item) -> toItemForOwnerDto(item,
                        BookingMapper.toBookingDto(itemService.getLastBooking(item.getId())),
                        BookingMapper.toBookingDto(itemService.getNextBooking(item.getId())),
                        CommentMapper.toCommentDtoList(itemService.getComments(item.getId()))))
                .collect(Collectors.toList());
    }
    public static List<ItemDto> toItemDtoList(List<Item> itemList) {
        return itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
