package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private final ItemService itemService;

    @PostMapping
    public ItemRequestDto addItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        ItemRequest newItemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        ItemRequest itemRequest = itemRequestService.addRequest(newItemRequest, userId);
        return ItemRequestMapper.toItemRequestDto(itemRequest, new ArrayList<>());
    }

    @GetMapping
    public List<ItemRequestDto> getOwnItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return ItemRequestMapper.toItemRequestDtoList(itemRequestService.getOwnRequest(userId), itemService);
    }

//    @GetMapping("/all")
//    public List<ItemRequestDto> getItemRequests(@RequestHeader("X-Sharer-User-Id", @RequestParam) long userId) {
//        return ItemRequestMapper.toItemRequestDtoList(itemRequestService.getAllRequests(userId), itemService);
//    }
}
