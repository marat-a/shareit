package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    private  final User user1 = new User(1L, "Eugene", "eugene@mail.ru");
    private  final User user2 = new User(2L, "Petr", "petr@mail.ru");
    private  final ItemRequest itemRequest1 = new ItemRequest(1L, "ищу сувенир", user2, LocalDateTime.now().plusSeconds(30));
    private  final Item item2 = new Item(2L, user1, "Открытка", "сувенир2", true, itemRequest1);
    private  final ItemRequest itemRequest2 = new ItemRequest(2L, "ищу сувенир1", user2, LocalDateTime.now().plusSeconds(40));
    private final Item item1 = new Item(1L, user1, "Магнит", "сувенир1", true, null);
    private final Comment comment1 = new Comment(1L, "комментарий1", item1, user2, LocalDateTime.now());
    @Mock
    ItemRepository mockItemRepository;
    @Mock
    CommentRepository mockCommentRepository;
    @Mock
    UserService mockUserService;
    @Mock
    ItemRequestService mockItemRequestService;
    @Mock
    BookingService mockBookingService;

    ItemServiceImpl itemServiceImpl;

    @BeforeEach
    void setUp() {
        itemServiceImpl = new ItemServiceImpl(mockItemRepository,
                mockUserService,
                mockCommentRepository,
                mockBookingService,
                mockItemRequestService);
    }

    @Test
    @DisplayName("Получить список своих вещей")
    void getItemsByUserId() {
        List<Item> sourceItems = List.of(item1, item2);
        Mockito
                .when(mockItemRepository.findItemsByOwnerId(anyLong()))
                .thenReturn(sourceItems);

        List<Item> targetItems = itemServiceImpl.getItemsByUserId(user1.getId());
        Assertions.assertEquals(sourceItems.size(), targetItems.size());
        checkFields(sourceItems, targetItems);
    }

    @Test
    @DisplayName("Получить список своих вещей с пагинацией")
    void getItemsByUserIdWithPagination() {
        List<Item> sourceItems = List.of(item1, item2);
        Mockito
                .when(mockItemRepository.findItemsByOwnerIdOrderById(anyLong(), any()))
                .thenReturn(sourceItems);

        List<Item> targetItems = itemServiceImpl.getItemsByUserId(user1.getId(), 0, 20);
        Assertions.assertEquals(sourceItems.size(), targetItems.size());
        checkFields(sourceItems, targetItems);
    }

    @Test
    @DisplayName("Получить вещь по id")
    void getItemId() {
        Mockito
                .when(mockUserService.isUserExists(anyLong()))
                .thenReturn(true);

        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item1));

        Item targetItem = itemServiceImpl.getItemById(user1.getId(), item1.getId());
        checkFields(targetItem, item1);
    }

    @Test
    @DisplayName("Добавить вещь")
    void addItem() {
        Item newItem = copyItem();
        newItem.setId(null);
        newItem.setOwner(null);
        newItem.setRequest(null);
        Mockito
                .when(mockUserService.isUserExists(anyLong()))
                .thenReturn(true);
        Mockito
                .when(mockUserService.getUser(anyLong()))
                .thenReturn(user1);
        Mockito
                .when(mockItemRequestService.getRequest(anyLong(), anyLong()))
                .thenReturn(itemRequest1);
        Mockito
                .when(mockItemRepository.save(any()))
                .thenReturn(item2);

        Item targetItem = itemServiceImpl.addItem(user1.getId(), newItem, itemRequest1.getId());
        checkFields(targetItem, item2);
    }

    @Test
    @DisplayName("Обновить данные о вещи")
    void updateItem() {
        String newName = item2.getName() + "1";
        String newDescription = item2.getDescription() + "1";
        Boolean newAvailable = !item2.getAvailable();
        Item sourceItem = copyItem();
        sourceItem.setName(newName);
        sourceItem.setDescription(newDescription);
        sourceItem.setAvailable(newAvailable);
        sourceItem.setRequest(itemRequest2);

        Item newItem = new Item();
        newItem.setName(newName);
        newItem.setDescription(newDescription);
        newItem.setAvailable(newAvailable);
        newItem.setRequest(itemRequest2);
        Mockito
                .when(mockUserService.isUserExists(anyLong()))
                .thenReturn(true);

        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item2));

        Mockito
                .when(mockItemRepository.save(any()))
                .thenReturn(sourceItem);

        Item targetItem = itemServiceImpl.updateItem(user1.getId(), item2.getId(), newItem);
        checkFields(targetItem, sourceItem);
    }

    @Test
    @DisplayName("Обновить название вещи")
    void updateItemName() {
        String newName = item2.getName() + "1";
        Item sourceItem = copyItem();
        sourceItem.setName(newName);

        Item newItem = new Item();
        newItem.setName(newName);
        Mockito
                .when(mockUserService.isUserExists(anyLong()))
                .thenReturn(true);
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item2));
        Mockito
                .when(mockItemRepository.save(any()))
                .thenReturn(sourceItem);

        Item targetItem = itemServiceImpl.updateItem(user1.getId(), item2.getId(), newItem);
        checkFields(targetItem, sourceItem);
    }

    @Test
    @DisplayName("Обновить описание вещи")
    void updateItemDescription() {
        String newDescription = item2.getDescription() + "1";
        Item sourceItem = copyItem();
        sourceItem.setDescription(newDescription);

        Item newItem = new Item();
        newItem.setDescription(newDescription);
        Mockito
                .when(mockUserService.isUserExists(anyLong()))
                .thenReturn(true);
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item2));
        Mockito
                .when(mockItemRepository.save(any()))
                .thenReturn(sourceItem);

        Item targetItem = itemServiceImpl.updateItem(user1.getId(), item2.getId(), newItem);
        checkFields(targetItem, sourceItem);
    }

    @Test
    @DisplayName("Обновить статус вещи")
    void updateItemAvailable() {
        Boolean newAvailable = !item2.getAvailable();
        Item sourceItem = copyItem();
        sourceItem.setAvailable(newAvailable);

        Item newItem = new Item();
        newItem.setAvailable(newAvailable);
        Mockito
                .when(mockUserService.isUserExists(anyLong()))
                .thenReturn(true);
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item2));
        Mockito
                .when(mockItemRepository.save(any()))
                .thenReturn(sourceItem);

        Item targetItem = itemServiceImpl.updateItem(user1.getId(), item2.getId(), newItem);
        checkFields(targetItem, sourceItem);
    }

    @Test
    @DisplayName("Обновить запрос вещи")
    void updateItemRequest() {
        Item sourceItem = copyItem();
        sourceItem.setRequest(itemRequest2);

        Item newItem = new Item();
        Mockito
                .when(mockUserService.isUserExists(anyLong()))
                .thenReturn(true);
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item2));
        Mockito
                .when(mockItemRepository.save(any()))
                .thenReturn(sourceItem);

        Item targetItem = itemServiceImpl.updateItem(user1.getId(), item2.getId(), newItem);
        checkFields(targetItem, sourceItem);
    }

    @Test
    @DisplayName("Найти вещь по названию или описанию")
    void getItemsByText() {
        List<Item> sourceItems = List.of(item1, item2);
        Mockito
                .when(mockItemRepository.findAllByNameOrDescriptionContainsIgnoreCase(anyString(), any()))
                .thenReturn(sourceItems);
        Mockito
                .when(mockUserService.isUserExists(anyLong()))
                .thenReturn(true);
        List<Item> targetItems = itemServiceImpl.getItemsByText(user1.getId(), "сувенир", 0, 20);
        Assertions.assertEquals(sourceItems.size(), targetItems.size());
        checkFields(sourceItems, targetItems);
    }

    @Test
    @DisplayName("Добавить отзыв")
    void addComment() {

        Mockito
                .when(mockBookingService.isUserBookedItem(anyLong(), anyLong()))
                .thenReturn(true);
        Mockito
                .when(mockCommentRepository.save(any()))
                .thenReturn(comment1);

        Comment targetComment = itemServiceImpl.addComment(comment1);
        assertThat(targetComment.getId(), equalTo(comment1.getId()));
        assertThat(targetComment.getText(), equalTo(comment1.getText()));
        assertThat(targetComment.getItem(), equalTo(comment1.getItem()));
        assertThat(targetComment.getAuthor(), equalTo(comment1.getAuthor()));
        assertThat(targetComment.getCreated(), equalTo(comment1.getCreated()));
    }

    @Test
    @DisplayName("Получить отзывы о вещи")
    void getCommentsByItemId() {
        List<Comment> sourceComments = List.of(comment1);
        Mockito
                .when(mockCommentRepository.findAllByItemId(anyLong()))
                .thenReturn(Optional.of(sourceComments));

        List<Comment> targetComments = itemServiceImpl.getComments(item2.getId());
        Assertions.assertEquals(sourceComments.size(), targetComments.size());
        for (Comment comment : sourceComments) {
            assertThat(targetComments, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("text", equalTo(comment.getText())),
                    hasProperty("item", equalTo(comment.getItem())),
                    hasProperty("author", equalTo(comment.getAuthor())),
                    hasProperty("created", equalTo(comment.getCreated()))
            )));
        }
    }

    @Test
    @DisplayName("Получить список вещей, созданных по запросу")
    void getItemsByRequestId() {
        List<Item> sourceItems = List.of(item2);
        Mockito
                .when(mockItemRepository.findAllByRequestId(anyLong()))
                .thenReturn(sourceItems);

        List<Item> targetItems = itemServiceImpl.getItemByItemRequestId(itemRequest1.getId());
        Assertions.assertEquals(sourceItems.size(), targetItems.size());
        checkFields(sourceItems, targetItems);
    }

    private void checkFields(Item targetItem, Item sourceItem) {
        assertThat(targetItem.getId(), equalTo(sourceItem.getId()));
        assertThat(targetItem.getName(), equalTo(sourceItem.getName()));
        assertThat(targetItem.getDescription(), equalTo(sourceItem.getDescription()));
        assertThat(targetItem.getAvailable(), equalTo(sourceItem.getAvailable()));
        assertThat(targetItem.getOwner(), equalTo(sourceItem.getOwner()));
        assertThat(targetItem.getRequest(), equalTo(sourceItem.getRequest()));
    }

    private void checkFields(List<Item> sourceItems, List<Item> targetItems) {
        for (Item item : sourceItems) {
            assertThat(targetItems, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(item.getName())),
                    hasProperty("description", equalTo(item.getDescription())),
                    hasProperty("available", equalTo(item.getAvailable())),
                    hasProperty("owner", equalTo(item.getOwner())),
                    hasProperty("request", equalTo(item.getRequest()))
            )));
        }
    }

    private Item copyItem() {
        Item newItem = new Item();
        newItem.setId(item2.getId());
        newItem.setName(item2.getName());
        newItem.setDescription(item2.getDescription());
        newItem.setAvailable(item2.getAvailable());
        newItem.setOwner(item2.getOwner());
        newItem.setRequest(item2.getRequest());
        return newItem;
    }
}