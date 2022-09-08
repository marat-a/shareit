package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.item.model.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.model.dto.CommentDto;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.ItemForOwnerDto;
import ru.practicum.shareit.item.model.dto.NewCommentDTO;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;
    private final ItemRequestService itemRequestService;

    @GetMapping
    public List<ItemForOwnerDto> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        List<Item> items = itemService.getItemsByUserId(userId);
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
                           @Valid @RequestBody ItemDto itemDto) {

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
                                        @RequestParam String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return ItemMapper.toItemDtoList(itemService.getItemsByText(userId, text));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody @Valid NewCommentDTO newCommentDTO) {
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