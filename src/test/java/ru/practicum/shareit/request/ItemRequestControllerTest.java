package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;

    @MockBean
    ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private final User user1 = new User(1L, "Eugene", "eugene@mail.ru");
    private final User user2 = new User(2L, "Petr", "petr@mail.ru");

    private final ItemRequest itemRequest1 = new ItemRequest(1L, "нужен дипломат", user1, LocalDateTime.now());
    private final ItemRequest itemRequest2 = new ItemRequest(2L, "разыскивается трюмо", user1, LocalDateTime.now().plusSeconds(30));

    private final Item item1 = new Item(1L, user2, "Трюмо", "советское трюмо", true, itemRequest2);

    @Test
    @DisplayName("Добавить запрос")
    void addItemRequest() throws Exception {
        when(itemRequestService.addRequest(any(), anyLong()))
                .thenReturn(itemRequest1);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", itemRequest1.getRequestor().getId())
                        .content(mapper.writeValueAsString(ItemRequestMapper.toItemRequestDto(itemRequest1, itemService)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequest1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequest1.getDescription())))
                .andExpect(jsonPath("$.requestor.id", is(itemRequest1.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$.items", is(nullValue())));
    }

    @Test
    @DisplayName("Получить список своих запросов")
    void getItemRequests() throws Exception {
        when(itemRequestService.getOwnRequest(anyLong()))
                .thenReturn(List.of(itemRequest1, itemRequest2));
        when(itemService.getItemByItemRequestId(anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long itemRequestId = invocationOnMock.getArgument(0, Long.class);
                    if (itemRequestId == 2) {
                        return List.of(item1);
                    }
                    return null;
                });

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", user1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequest1.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequest1.getDescription())))
                .andExpect(jsonPath("$[0].requestor.id", is(itemRequest1.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$[0].items", is(nullValue())))
                .andExpect(jsonPath("$[1].id", is(itemRequest2.getId()), Long.class))
                .andExpect(jsonPath("$[1].description", is(itemRequest2.getDescription())))
                .andExpect(jsonPath("$[1].requestor.id", is(itemRequest2.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$[1].items[0].id", is(item1.getId()), Long.class));

    }

    @Test
    @DisplayName("Получить списка всех запросов")
    void getItemRequestsAll() throws Exception {
        when(itemRequestService.getAllItemRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemRequest1, itemRequest2));
        when(itemService.getItemByItemRequestId(anyLong()))
                .thenAnswer(invocationOnMock -> {
                    long itemRequestId = invocationOnMock.getArgument(0, Long.class);
                    if (itemRequestId == 2) {
                        return List.of(item1);
                    }
                    return null;
                });

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", user2.getId())
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequest1.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequest1.getDescription())))
                .andExpect(jsonPath("$[0].requestor.id", is(itemRequest1.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$[0].items", is(nullValue())))
                .andExpect(jsonPath("$[1].id", is(itemRequest2.getId()), Long.class))
                .andExpect(jsonPath("$[1].description", is(itemRequest2.getDescription())))
                .andExpect(jsonPath("$[1].requestor.id", is(itemRequest2.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$[1].items[0].id", is(item1.getId()), Long.class));
    }

    @Test
    @DisplayName("Получить данные о запросе по id")
    void getItemRequest() throws Exception {
        when(itemRequestService.getRequest(anyLong(), anyLong()))
                .thenReturn(itemRequest1);
        when(itemService.getItemByItemRequestId(anyLong()))
                .thenReturn(null);

        mvc.perform(get("/requests/{requestId}", itemRequest1.getId())
                        .header("X-Sharer-User-Id", itemRequest1.getRequestor().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequest1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequest1.getDescription())))
                .andExpect(jsonPath("$.requestor.id", is(itemRequest1.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$.items", is(nullValue())));

    }

    @Test
    @DisplayName("Получение запроса с данными о вещах")
    void getItemRequestWithItems() throws Exception {
        when(itemRequestService.getRequest(anyLong(), anyLong()))
                .thenReturn(itemRequest2);
        when(itemService.getItemByItemRequestId(anyLong()))
                .thenReturn(List.of(item1));

        mvc.perform(get("/requests/{requestId}", itemRequest2.getId())
                        .header("X-Sharer-User-Id", itemRequest2.getRequestor().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequest2.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequest2.getDescription())))
                .andExpect(jsonPath("$.requestor.id", is(itemRequest2.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$.items[0].id", is(item1.getId()), Long.class));
    }
}