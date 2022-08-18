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

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public Item addItem(long userId, Item item) {
        if (userService.isUserExists(userId)) {
            item.setOwner(userService.getUser(userId
            ));
            return itemRepository.save(item);
        } else throw new NotFoundException("Пользователь с таким id не найден");
    }

    @Override
    public Item updateItem(long userId, long itemId, Item newItem) {
        if (isUserAddItem(userId, itemId)) {
            Item item = getItemById(userId, itemId);
            if (newItem.getAvailable() != null) {
                item.setAvailable(newItem.getAvailable());
            }
            if (newItem.getDescription() != null) {
                item.setDescription(newItem.getDescription());
            }
            if (newItem.getName() != null) {
                item.setName(newItem.getName());
            }
            return itemRepository.save(item);
        } else
            throw new NotFoundException("У Пользователя с id " + userId + " не найдена вещь c id " + itemId + "!");

    }

    @Override
    public List<Item> getItemsByText(long userId, String text) {
        if (userService.isUserExists(userId)) {
            return itemRepository.findAllByNameOrDescriptionContainsIgnoreCase(text);
        } else throw new ValidationException("Пользователь с таким id не найден");
    }

    @Override
    public List<Item> getItemsByUserId(long userId) {
        return itemRepository.findItemsByOwnerId(userId);
    }

    @Override
    public Item getItemById(long userId, long itemId) {
        if (userService.isUserExists(userId)) {
            return itemRepository.findById(itemId);
        } else throw new NotFoundException("Пользователь с таким id не найден");
    }

    @Override
    public boolean isUserAddItem(long userId, long itemId) {
        return getItemById(userId, itemId).getOwner().getId() == userId;
    }
}