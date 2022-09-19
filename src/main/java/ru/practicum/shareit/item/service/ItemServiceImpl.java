package ru.practicum.shareit.item.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final BookingService bookingService;
    private final ItemRequestService itemRequestService;

    public ItemServiceImpl(ItemRepository itemRepository, UserService userService, CommentRepository commentRepository, @Lazy BookingService bookingService, @Lazy ItemRequestService itemRequestService) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.bookingService = bookingService;
        this.itemRequestService = itemRequestService;
    }


    @Override
    public Item addItem(Long userId, Item item, Long requestId) {
        if (userService.isUserExists(userId)) {
            item.setOwner(userService.getUser(userId));
            if (requestId != null){
                item.setRequest(itemRequestService.getRequest(userId, requestId));
            }
            return itemRepository.save(item);
        } else throw new NotFoundException("Пользователь с таким id не найден");
    }

    @Override
    public Item updateItem(Long userId, Long itemId, Item newItem) {
        if (isUserAddItem(userId, itemId)) {
            Item item = getItemById(userId, itemId);
            if (newItem.getAvailable() != null) {
                item.setAvailable(newItem.getAvailable());
            }
            if (newItem.getDescription() != null) {
                item.setDescription(newItem.getDescription());
            }
            if (newItem.getName() != null) {
                item.setName(newItem.getName());
            }
            return itemRepository.save(item);
        } else
            throw new NotFoundException("У Пользователя с id " + userId + " не найдена вещь c id " + itemId + "!");

    }

    @Override
    public List<Item> getItemsByText(Long userId, String text, int from, int size) {
        if (userService.isUserExists(userId)) {
            PageRequest pageRequest = PageRequest.of(from/size, size);
            return itemRepository.findAllByNameOrDescriptionContainsIgnoreCase(text, pageRequest);
        } else throw new ValidationException("Пользователь с таким id не найден");
    }

    @Override
    public List<Item> getItemsByUserId(Long userId, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from/size, size);
        return itemRepository.findItemsByOwnerIdOrderById(userId, pageRequest);
    }

    @Override
    public Item getItemById(Long userId, Long itemId) {
        if (userService.isUserExists(userId)) {
            return itemRepository.findById(itemId)
                    .orElseThrow(() -> new NotFoundException("Вещь с таким id не найдена"));
        } else throw new NotFoundException("Пользователь с таким id не найден");
    }

    @Override
    public boolean isUserAddItem(Long userId, Long itemId) {
        return getItemById(userId, itemId).getOwner().getId().equals(userId);
    }

    @Override
    public boolean isItemExist(Long itemId) {
        return itemRepository.existsById(itemId);
    }

    @Override
    public Comment addComment(Comment comment){
        if (bookingService.isUserBookedItem(comment.getItem().getId(), comment.getAuthor().getId())){
            return commentRepository.save(comment);
        } else throw new BadRequestException("Пользователь не брал вещь в аренду");
    }

    @Override
    public List<Comment> getComments(long itemId){
            return commentRepository.findAllByItemId(itemId).orElse(new ArrayList<>());
    }

    @Override
    public Booking getLastBooking(long itemId){
        return bookingService.getLastBookingByItemId(itemId);
    }

    @Override
    public Booking getNextBooking(long itemId){
        return bookingService.getNextBookingByItemId(itemId);
    }

    @Override
    public List<Item> getItemByItemRequestId(long requestId) {
        return itemRepository.findAllByRequestId(requestId);
    }

    @Override
    public List<Item> getItemsByUserId(Long id) {
        return itemRepository.findItemsByOwnerId(id);
    }

    public Item getItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(()->new NotFoundException("Вещь с таким id не найдена"));
    }
}