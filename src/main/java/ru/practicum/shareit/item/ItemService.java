package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {

    List<Item> getItemsByUserId(Long userId);

    Item getItemById(Long userId, Long itemId);

    Item addItem(Long userId, Item item);

    Item updateItem(Long userId, Long itemId, Item item);

    List<Item> getItemsByText(Long userId, String text);

    boolean isUserAddItem(Long userId, Long itemId);

    boolean isItemExist (Long itemId);

    Item getItem(Long itemId);
}
