package ru.practicum.server.request.service;

import ru.practicum.server.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest addRequest(ItemRequest newItemRequest, long userId);

    ItemRequest getRequest(long userId, long itemRequestId);

    List<ItemRequest> getOwnRequest(long userId);

    List<ItemRequest> getAllItemRequests(long userId, int from, int size);
}
