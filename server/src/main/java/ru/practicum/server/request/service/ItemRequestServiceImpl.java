package ru.practicum.server.request.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.exceptions.NotFoundException;
import ru.practicum.server.item.service.ItemService;
import ru.practicum.server.request.ItemRequestRepository;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.user.UserService;

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
    public ItemRequest getRequest(long userId, long itemRequestId) {
        if (userService.isUserExists(userId)) {
            return itemRequestRepository.findById(itemRequestId)
                    .orElseThrow(() -> new NotFoundException("Запрос с id " + itemRequestId + " не найден"));
        } else throw new NotFoundException("Пользователь с id " + userId + " не найден");
    }

    @Override
    public List<ItemRequest> getOwnRequest(long userId) {
        if (userService.isUserExists(userId)) {
            return itemRequestRepository.findItemRequestsByRequestor_IdOrderByCreated(userId);
        } else throw new NotFoundException("Пользователь с id " + userId + " не найден");
    }

    @Override
    public List<ItemRequest> getAllItemRequests(long userId, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return itemRequestRepository.findAllByRequestor_IdNot(userId, pageRequest).getContent();

    }

}
