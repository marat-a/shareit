package ru.practicum.server.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.item.service.ItemService;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.request.model.ItemRequestDto;
import ru.practicum.server.request.model.ItemRequestMapper;
import ru.practicum.server.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private final ItemService itemService;

    @PostMapping
    public ItemRequestDto addItemRequest(
            @RequestBody ItemRequestDto itemRequestDto,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        ItemRequest newItemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        ItemRequest itemRequest = itemRequestService.addRequest(newItemRequest, userId);
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @GetMapping
    public List<ItemRequestDto> getOwnItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return ItemRequestMapper.toItemRequestDtoList(itemRequestService.getOwnRequest(userId), itemService);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(defaultValue = "0", required = false) int from,
            @RequestParam(defaultValue = "10", required = false) int size) {
        return ItemRequestMapper.toItemRequestDtoList(itemRequestService.getAllItemRequests(userId, from, size), itemService);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable int requestId) {
        return ItemRequestMapper.toItemRequestDto(
                itemRequestService.getRequest(userId, requestId), itemService);
    }
}
