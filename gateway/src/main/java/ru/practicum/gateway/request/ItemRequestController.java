package ru.practicum.gateway.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

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
    public ResponseEntity<Object> getRequestsByUserId(
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get own ItemRequest, userId = {}", userId);
        return itemRequestclient.getRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero(message = "From must be equal or more than 0") int from,
            @RequestParam(defaultValue = "20", required = false) @Positive (message = "Size must be more than 0") int size) {
        log.info("Get all ItemRequests, userId = {}", userId);
        return itemRequestclient.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long requestId) {
        log.info("Get ItemRequest  with id {} , userId = {}", requestId, userId);
        return itemRequestclient.getRequestById(userId, requestId);
    }
}
