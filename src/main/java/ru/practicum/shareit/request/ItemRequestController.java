package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private final ItemService itemService;

    @PostMapping
    public ItemRequestDto addItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") long userId) {
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
            @RequestParam (defaultValue = "0", required = false) @Min(value = 0, message = "From must be equal or more than 0")int from,
            @RequestParam (defaultValue = "10", required = false) @Min(value = 1, message = "Size must be more than 0") int size) {
        return ItemRequestMapper.toItemRequestDtoList(itemRequestService.getAllItemRequests (userId, from, size), itemService);
    }

    @GetMapping("/{requestId}")

    public ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable int requestId) {
        return ItemRequestMapper.toItemRequestDto(itemRequestService.getRequest(userId ,requestId), itemService);
    }
}
