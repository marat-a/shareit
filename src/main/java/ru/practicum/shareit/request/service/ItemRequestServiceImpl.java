package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    ItemService itemService;
    ItemRequestRepository itemRequestRepository;
    UserService userService;

    @Override
    public ItemRequest addRequest(ItemRequest newItemRequest, long userId) {
        newItemRequest.setRequestor(userService.getUser(userId));
        newItemRequest.setCreated(LocalDateTime.now());
        return itemRequestRepository.save(newItemRequest);
    }

    @Override
    public ItemRequest getRequest(long itemRequestId) {
        return itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id " + itemRequestId + " не найден"));
    }

    @Override
    public List<ItemRequest> getOwnRequest(long userId) {
        if (userService.isUserExists(userId)) {
            return itemRequestRepository.findItemRequestsByRequestor_IdOrderByCreated(userId);
        } else throw new NotFoundException("Пользователь с id " + userId + " не найден");
    }

}
