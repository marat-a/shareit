package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return ItemMapper.toItemDtoList(itemService.getItemsByUserId(userId));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemByUserId(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @PathVariable long itemId) {
        return ItemMapper.toItemDto(itemService.getItemId(userId, itemId));
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @Valid @RequestBody ItemDto itemDto) {
        return ItemMapper.toItemDto(itemService.addItem(userId, ItemMapper.toItem(itemDto, userId)));
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable long itemId,
                              @RequestBody ItemDto itemDto) {
        return ItemMapper.toItemDto(itemService.updateItem(userId, itemId, ItemMapper.toItem(itemDto, userId)));
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByText(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestParam String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return ItemMapper.toItemDtoList(itemService.getItemsByText(userId, text));
    }
}