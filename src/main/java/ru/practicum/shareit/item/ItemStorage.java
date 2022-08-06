package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;

import java.util.List;


public interface ItemStorage {

    Item addItem(long userId, Item item);
    Item editItem(long userId, Item item);
    List<Item> getItemsByUserId(long userId);
    Item getItemById(long itemId);
    List<Item> searchItemsByText(String text);
}