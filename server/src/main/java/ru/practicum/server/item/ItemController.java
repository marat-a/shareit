package ru.practicum.server.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.booking.model.BookingMapper;
import ru.practicum.server.item.model.CommentMapper;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.model.ItemMapper;
import ru.practicum.server.item.model.dto.CommentDto;
import ru.practicum.server.item.model.dto.ItemDto;
import ru.practicum.server.item.model.dto.ItemForOwnerDto;
import ru.practicum.server.item.model.dto.NewCommentDTO;
import ru.practicum.server.item.service.ItemService;
import ru.practicum.server.user.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;

    @GetMapping
    public List<ItemForOwnerDto> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @RequestParam(defaultValue = "0", required = false) int from,
                                          @RequestParam(defaultValue = "10", required = false) int size) {
        List<Item> items = itemService.getItemsByUserId(userId, from, size);
        return ItemMapper.toItemForOwnerDtoList(items, itemService);
    }

    @GetMapping("/{itemId}")
    public ItemForOwnerDto getItemByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @PathVariable Long itemId) {
        if (itemService.isUserAddItem(userId, itemId)) {
            return ItemMapper.toItemForOwnerDto(
                    itemService.getItemById(userId, itemId),
                    BookingMapper.toBookingDto(itemService.getLastBooking(itemId)),
                    BookingMapper.toBookingDto(itemService.getNextBooking(itemId)),
                    CommentMapper.toCommentDtoList(
                            itemService.getComments(itemId)
                    )
            );
        } else return ItemMapper.toItemForOwnerDto(
                itemService.getItemById(userId, itemId),
                null,
                null,
                CommentMapper.toCommentDtoList(
                        itemService.getComments(itemId)
                )
        );
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @RequestBody ItemDto itemDto) {

        Item newItem = ItemMapper.toItem(itemDto);
        Item itemFromDb = itemService.addItem(userId, newItem, itemDto.getRequestId());
        return ItemMapper.toItemDto(itemFromDb);
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                            @PathVariable Long itemId,
                            @RequestBody ItemDto itemDto) {
        return ItemMapper.toItemDto(itemService.updateItem(
                userId,
                itemId,
                ItemMapper.toItem(itemDto)
        ), CommentMapper.toCommentDtoList(itemService.getComments(itemId)));
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByText(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestParam String text,
                                        @RequestParam(defaultValue = "0", required = false) int from,
                                        @RequestParam(defaultValue = "10", required = false) int size) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return ItemMapper.toItemDtoList(itemService.getItemsByText(userId, text, from, size));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody NewCommentDTO newCommentDTO) {
        return CommentMapper.toCommentDto(
                itemService.addComment(
                        CommentMapper.toComment(
                                newCommentDTO,
                                userService.getUser(userId),
                                itemService.getItem(itemId)
                        )
                )
        );
    }
}