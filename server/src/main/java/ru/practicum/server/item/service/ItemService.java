package ru.practicum.server.item.service;

import ru.practicum.server.item.model.Comment;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.booking.model.Booking;

import java.util.List;

public interface ItemService {

    List<Item> getItemsByUserId(Long userId, int from, int size);

    Item getItemById(Long userId, Long itemId);

    Item addItem(Long userId, Item item, Long requestId);

    Item updateItem(Long userId, Long itemId, Item item);

    List<Item> getItemsByText(Long userId, String text, int from, int size);

    boolean isUserAddItem(Long userId, Long itemId);

    boolean isItemExist (Long itemId);

    Item getItem(Long itemId);

    Comment addComment(Comment comment);

    List<Comment> getComments(long itemId);

    Booking getLastBooking(long itemId);

    Booking getNextBooking(long itemId);

    List<Item> getItemByItemRequestId (long requestId);

    List<Item> getItemsByUserId(Long id);
}
