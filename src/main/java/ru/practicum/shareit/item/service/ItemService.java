package ru.practicum.shareit.item.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    List<Item> getItemsByUserId(Long userId);

    Item getItemById(Long userId, Long itemId);

    Item addItem(Long userId, Item item, Long requestId);

    Item updateItem(Long userId, Long itemId, Item item);

    List<Item> getItemsByText(Long userId, String text);

    boolean isUserAddItem(Long userId, Long itemId);

    boolean isItemExist (Long itemId);

    Item getItem(Long itemId);

    Comment addComment(Comment comment);

    List<Comment> getComments(long itemId);

    Booking getLastBooking(long itemId);

    Booking getNextBooking(long itemId);

    List<Item> getItemByItemRequestId (long requestId);

}
