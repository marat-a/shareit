package ru.practicum.server.item.model;

import lombok.AllArgsConstructor;
import ru.practicum.server.item.model.dto.ShortItemDto;
import ru.practicum.server.booking.model.BookingMapper;
import ru.practicum.server.booking.model.dto.BookingDto;
import ru.practicum.server.item.model.dto.CommentDto;
import ru.practicum.server.item.model.dto.ItemDto;
import ru.practicum.server.item.model.dto.ItemForOwnerDto;
import ru.practicum.server.item.service.ItemService;
import ru.practicum.server.request.service.ItemRequestService;
import ru.practicum.server.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ItemMapper {

    private final UserService userService;
    private final ItemService itemService;
    private  final ItemRequestService itemRequestService;


    public static ItemDto toItemDto(Item item, List<CommentDto> comments) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() == null ? null : item.getRequest().getId(),
                comments
        );
    }
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() == null ? null : item.getRequest().getId(),
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

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
        item.setDescription(itemDto.getDescription());
        item.setName(itemDto.getName());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    public static List<ItemForOwnerDto> toItemForOwnerDtoList(List<Item> itemList, ItemService itemService) {
        if (itemList!=null)
        return itemList.stream()
                .map((Item item) -> toItemForOwnerDto(item,
                        BookingMapper.toBookingDto(itemService.getLastBooking(item.getId())),
                        BookingMapper.toBookingDto(itemService.getNextBooking(item.getId())),
                        CommentMapper.toCommentDtoList(itemService.getComments(item.getId()))))
                .collect(Collectors.toList());
        else return null;
    }

    public static List<ItemDto> toItemDtoList(List<Item> itemList) {
        if (itemList!=null)
        return itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        else return null;
    }

    public static ShortItemDto toShortItemDto(Item item) {
        return new ShortItemDto(
                item.getId(),
                item.getName(),
                item.getDescription()
        );
    }

    public static List<ShortItemDto> toShortItemDtoList(List<Item> itemList) {
        if (itemList!=null)
        return itemList.stream()
                .map(ItemMapper::toShortItemDto)
                .collect(Collectors.toList());
        else  return null;
    }
}
