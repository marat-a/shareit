package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest addRequest(ItemRequest newItemRequest, long userId);

    ItemRequest getRequest(long itemRequestId);

    List<ItemRequest> getOwnRequest(long userId);
}
