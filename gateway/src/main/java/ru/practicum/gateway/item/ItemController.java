package ru.practicum.gateway.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.item.dto.ItemDto;
import ru.practicum.gateway.item.dto.NewCommentDTO;
import ru.practicum.gateway.user.UserClient;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getItems(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "From must be equal or more than 0") int from,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 1, message = "Size must be more than 0") int size) {
        log.info("Get items to owner с userId={}, from={}, size={}", userId, from, size);
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId) {
        log.info("Get item by id = {}, userId = {}", itemId, userId);
        return itemClient.getItem(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @Valid @RequestBody ItemDto itemDto) {
        log.info("Add item {}, userId = {}", itemDto, userId);
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> editItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @PathVariable Long itemId,
                                           @RequestBody ItemDto itemDto) {
        log.info("Edit item {}, userId = {}", itemId, userId);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsByText(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam String text,
                                                 @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "From must be equal or more than 0") int from,
                                                 @RequestParam(defaultValue = "10", required = false) @Min(value = 1, message = "Size must be more than 0") int size) {
        log.info("Search item by text \"{}\", userId = {}", text, userId);
        return itemClient.getItemsByText(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody @Valid NewCommentDTO newCommentDTO) {
        log.info("Add comment \"{}\" to item {}, userId = {}", newCommentDTO, itemId, userId);
        return itemClient.addComment(newCommentDTO, userId, itemId);
    }
}
