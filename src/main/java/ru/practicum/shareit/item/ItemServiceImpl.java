package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserService userService;



    @Override
    public List<Item> getItemsByUserId(long userId) {
        return itemStorage.getItemsByUserId(userId);
    }

    @Override
    public Item getItemId(long userId, long itemId) {
        userService.checkUserIsExists(userId);
        return itemStorage.getItemById(itemId);
    }

    @Override
    public Item addItem(long userId, Item item) {
        if (userService.checkUserIsExists(userId)) {
            item.setOwner(userService.getUser(userId
            ));
            return itemStorage.addItem(userId, item);
        } else throw new NotFoundException("Пользователь с таким id не найден");
    }

    @Override
    public Item updateItem(long userId, long itemId, Item item) {
        if (userService.checkUserIsExists(userId)) {
            if (isUserAddItem(userId, itemId)) {
                throw new ValidationException("У Пользователя с id " + userId + " не существует item c id " + itemId + "!");
            }
            item.setId(itemId);
            return itemStorage.editItem(userId, item);
        } else throw new ValidationException("Пользователь с таким id не найден");
    }

    @Override
    public List<Item> getItemsByText(long userId, String text) {
        if (userService.checkUserIsExists(userId)) {
            return itemStorage.searchItemsByText(text);
        } else throw new ValidationException("Пользователь с таким id не найден");
    }

    @Override
    public boolean isUserAddItem(long userId, long itemId) {
        return itemStorage.getItemsByUserId(userId).stream().noneMatch(i -> i.getId() == itemId);
    }
}