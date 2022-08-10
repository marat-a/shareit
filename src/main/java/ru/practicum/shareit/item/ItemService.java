package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    List<Item> getItemsByUserId(long userId);

    Item getItemId(long userId, long itemId);

    Item addItem(long userId, Item item);

    Item updateItem(long userId, long itemId, Item item);

    List<Item> getItemsByText(long userId, String text);

    boolean isUserAddItem(long userId, long itemId);
}
