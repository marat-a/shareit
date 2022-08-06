package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemStorageInMemory implements ItemStorage {
    Map<Long, Map<Long, Item>> usersItemsMap = new HashMap<>();
    Long idCounter = 0L;

    public Long generateId() {
        return ++idCounter;
    }

    @Override
    public Item addItem(long userId, Item item) {
        item.setId(generateId());
        if (usersItemsMap.get(userId) == null) {
            Map<Long, Item> usersItems = new HashMap<>();
            usersItems.put(item.getId(), item);
            usersItemsMap.put(userId, usersItems);
        } else {
            usersItemsMap.get(userId).put(item.getId(), item);
        }
        return usersItemsMap.get(userId).get(item.getId());
    }

    @Override
    public Item editItem(long userId, Item item) {
        if (item.getAvailable() != null) {
            usersItemsMap.get(userId).get(item.getId()).setAvailable(item.getAvailable());
        }
        if (item.getName() != null) {
            usersItemsMap.get(userId).get(item.getId()).setName(item.getName());
        }
        if (item.getDescription() != null) {
            usersItemsMap.get(userId).get(item.getId()).setDescription(item.getDescription());
        }
        return usersItemsMap.get(userId).get(item.getId());
    }

    @Override
    public List<Item> getItemsByUserId(long userId) {
        if (usersItemsMap.get(userId)!=null) {
            List<Item> list = new ArrayList<>();
            for (Item item : usersItemsMap.get(userId).values()) {
                list.add(item);
            }
            return list;
        } else throw new NotFoundException("У пользователя нет добавленных вещей");
    }

    @Override
    public Item getItemById(long itemId) {
        return usersItemsMap.values()
                .stream()
                .flatMap(userItems -> userItems.values().stream())
                .filter(item -> item.getId() == itemId)
                .findAny()
                .orElseThrow(() -> new ValidationException("Не найдена ведь с id " + itemId));
    }

    @Override
    public List<Item> searchItemsByText(String text) {
        return usersItemsMap.values()
                .stream()
                .flatMap(userItems -> userItems.values().stream())
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }
}
