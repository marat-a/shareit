package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    ItemRequestRepository mockIemRequestRepository;
    @Mock
    UserService mockUserService;
    @Mock
    ItemService mockItemService;

    ItemRequestServiceImpl itemRequestService;

    private final User user1 = new User(1L, "Евгений", "eugene@mail.ru");
    private final User user2 = new User(2L, "Петр", "petr@mail.ru");

    private  final ItemRequest REQUEST1 = new ItemRequest(1L, "патефон с большой трубой", user1, LocalDateTime.now());
    private  final ItemRequest REQUEST2 = new ItemRequest(2L, "советские коньки", user2, LocalDateTime.now().plusMinutes(10));

    @BeforeEach
    void setUp() {
        itemRequestService = new ItemRequestServiceImpl(mockItemService, mockIemRequestRepository, mockUserService);
    }

    @Test
    @DisplayName("Добавить новый запрос")
    void addItemRequest() {
        ItemRequest newItemRequest = new ItemRequest(
                null,
                REQUEST1.getDescription(),
                REQUEST1.getRequestor(),
                REQUEST1.getCreated());
        Mockito
                .when(mockUserService.getUser(anyLong()))
                .thenReturn(user1);
        Mockito
                .when(mockIemRequestRepository.save(any()))
                .thenReturn(REQUEST1);

        ItemRequest targetItemRequest = itemRequestService.addRequest(newItemRequest, user1.getId());
        assertThat(targetItemRequest.getId(), equalTo(REQUEST1.getId()));
        assertThat(targetItemRequest.getDescription(), equalTo(REQUEST1.getDescription()));
        assertThat(targetItemRequest.getRequestor(), equalTo(REQUEST1.getRequestor()));
        assertThat(targetItemRequest.getCreated(), equalTo(REQUEST1.getCreated()));
    }

    @Test
    @DisplayName("Получить список своих запросов")
    void getItemRequestsByUserId() {
        List<ItemRequest> itemRequests = List.of(REQUEST1, REQUEST2);
        Mockito
                .when(mockIemRequestRepository.findItemRequestsByRequestor_IdOrderByCreated(anyLong()))
                .thenReturn(itemRequests);
        Mockito
                .when(mockUserService.isUserExists(anyLong()))
                .thenReturn(true);
        List<ItemRequest> targetItemRequests = itemRequestService.getOwnRequest(user1.getId());
        Assertions.assertEquals(itemRequests.size(), targetItemRequests.size());
        for (ItemRequest sourceItemRequest : itemRequests) {
            assertThat(targetItemRequests, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("description", equalTo(sourceItemRequest.getDescription())),
                    hasProperty("requestor", equalTo(sourceItemRequest.getRequestor())),
                    hasProperty("created", equalTo(sourceItemRequest.getCreated()))
            )));
        }
    }

    @Test
    @DisplayName("Получить список всех запросов")
    void getItemRequestsAll() {
        List<ItemRequest> itemRequests = List.of(REQUEST1, REQUEST2);
        Page<ItemRequest> itemRequestPage = new PageImpl<>(itemRequests);
        Mockito
                .when(mockIemRequestRepository.findAllByRequestor_IdNot(anyLong(), any()))
                .thenReturn(itemRequestPage);

        List<ItemRequest> targetItemRequests = itemRequestService.getAllItemRequests(user2.getId(), 0, 20);
        Assertions.assertEquals(itemRequests.size(), targetItemRequests.size());
        for (ItemRequest sourceItemRequest : itemRequests) {
            assertThat(targetItemRequests, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("description", equalTo(sourceItemRequest.getDescription())),
                    hasProperty("requestor", equalTo(sourceItemRequest.getRequestor())),
                    hasProperty("created", equalTo(sourceItemRequest.getCreated()))
            )));
        }
    }

    @Test
    @DisplayName("Получить запрос по id")
    void getItemRequest() {
        Mockito
                .when(mockIemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(REQUEST1));
        Mockito
                .when(mockUserService.isUserExists(anyLong()))
                .thenReturn(true);
        ItemRequest targetItemRequest = itemRequestService.getRequest(user1.getId(), REQUEST1.getId());
        assertThat(targetItemRequest.getId(), equalTo(REQUEST1.getId()));
        assertThat(targetItemRequest.getDescription(), equalTo(REQUEST1.getDescription()));
        assertThat(targetItemRequest.getRequestor(), equalTo(REQUEST1.getRequestor()));
        assertThat(targetItemRequest.getCreated(), equalTo(REQUEST1.getCreated()));
    }


}