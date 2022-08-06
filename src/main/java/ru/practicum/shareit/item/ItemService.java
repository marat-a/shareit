package yandex.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yandex.practicum.shareit.exceptions.NotFoundException;
import yandex.practicum.shareit.exceptions.ValidationException;
import yandex.practicum.shareit.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;



    public List<Item> getItemsByUserId(long userId) {
        return itemStorage.getItemsByUserId(userId);
    }

    public Item getItemId(long userId, long itemId) {
        userService.checkUserIsExists(userId);
        return itemStorage.getItemById(itemId);
    }

    public Item addItem(long userId, Item item) {
        if (userService.checkUserIsExists(userId)) {
            item.setOwner(userId);
            return itemStorage.addItem(userId, item);
        } else throw new NotFoundException("Пользователь с таким id не найден");
    }

    public Item updateItem(long userId, long itemId, Item item) {
        if (userService.checkUserIsExists(userId)) {
            if (isUserAddItem(userId, itemId)) {
                throw new ValidationException("У Пользователя с id " + userId + " не существует item c id " + itemId + "!");
            }
            item.setId(itemId);
            return itemStorage.editItem(userId, item);
        } else throw new ValidationException("Пользователь с таким id не найден");
    }

    public List<Item> getItemsByText(long userId, String text) {
        if (userService.checkUserIsExists(userId)) {
            return itemStorage.searchItemsByText(text);
        } else throw new ValidationException("Пользователь с таким id не найден");
    }

    private boolean isUserAddItem(long userId, long itemId) {
        return itemStorage.getItemsByUserId(userId).stream().noneMatch(i -> i.getId() == itemId);
    }
}