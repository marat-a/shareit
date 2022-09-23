package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemRequestController {

    private final ItemRequestClient itemRequestclient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(
            @Valid @RequestBody ItemRequestDto itemRequestDto,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Add ItemRequest  \"{}\" , userId = {}", itemRequestDto.getDescription(), userId);
        return itemRequestclient.addRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnItemRequests(
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get own ItemRequest, userId = {}", userId);
        return itemRequestclient.getOwnRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "From must be equal or more than 0") int from,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 1, message = "Size must be more than 0") int size) {
        log.info("Get all ItemRequests, userId = {}", userId);
        return itemRequestclient.getRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long requestId) {
        log.info("Get ItemRequest  with id {} , userId = {}", requestId, userId);
        return itemRequestclient.getRequest(userId, requestId);
    }
}
