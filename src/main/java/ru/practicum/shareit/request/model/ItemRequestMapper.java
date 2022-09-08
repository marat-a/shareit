package ru.practicum.shareit.request.model;


import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest, ItemService itemService) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                ItemMapper.toItemDtoList(itemService.getItemByItemRequestId(itemRequest.getId())));
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                new ArrayList<>());
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        return itemRequest;
    }

    public static List<ItemRequestDto> toItemRequestDtoList(List<ItemRequest> itemRequestList, ItemService itemService) {

        return itemRequestList.stream()
                .map((ItemRequest itemRequest) -> toItemRequestDto(itemRequest,itemService))
                .collect(Collectors.toList());
    }


}
