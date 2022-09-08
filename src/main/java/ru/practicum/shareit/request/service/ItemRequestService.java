package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest addRequest(ItemRequest newItemRequest, long userId);

    ItemRequest getRequest(long userId, long itemRequestId);

    List<ItemRequest> getOwnRequest(long userId);

    List<ItemRequest> getAllItemRequests(long userId, int from, int size);
}
