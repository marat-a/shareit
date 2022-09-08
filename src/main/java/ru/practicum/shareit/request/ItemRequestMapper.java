package ru.practicum.shareit.request;


import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<Item> itemList) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                ItemMapper.toItemDtoList(itemList));
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        return itemRequest;
    }

    public static List<ItemRequestDto> toItemRequestDtoList(List<ItemRequest> itemRequestList, ItemService itemService) {

        return itemRequestList.stream()
                .map((ItemRequest itemRequest) -> toItemRequestDto(itemRequest,itemService.getItemByItemRequestId(itemRequest.getId())))
                .collect(Collectors.toList());
    }


}
