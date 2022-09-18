package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.NewBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private final User user1 = new User(1L, "Eugene", "eugene@mail.ru");
    private final User user2 = new User(2L, "Petr", "petr@mail.ru");
    private final Item item1 = new Item(1L, user1, "Магнит", "сувенир", true, null);
    private final Booking booking1 = new Booking(1L,
            LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS), LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS), item1, user2, Status.WAITING);
    private final Item item2 = new Item(2L, user1, "Открытка", "сувенир", true, null);
    private final Booking booking2 = new Booking(2L,
            LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS), LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS), item2, user2, Status.WAITING);
    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Получить информацию о бронировании по id")
    void getBookingById() throws Exception {
        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(booking1);

        mvc.perform(get("/bookings/{bookingId}", booking1.getId())
                        .header("X-Sharer-User-Id", booking1.getBooker().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking1.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(booking1.getStart().format(formatter))))
                .andExpect(jsonPath("$.end", is(booking1.getEnd().format(formatter))))
                .andExpect(jsonPath("$.item.id", is(booking1.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(booking1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking1.getStatus().toString())));
    }

    @Test
    @DisplayName("Получить бронирования для своих вещей ")
    void getBookingsByOwnerId() throws Exception {
        List<Booking> bookings = List.of(booking1, booking2);
        when(bookingService.getBookingToOwner(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(bookings);

        List<BookingDto> bookingResponseDtoList = BookingMapper.toBookingDtoList(bookings);
        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", booking1.getItem().getOwner().getId())
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingResponseDtoList)));
    }

    @Test
    @DisplayName("Получить свои бронирования")
    void getBookingsByBookerId() throws Exception {
        List<Booking> bookings = List.of(booking1, booking2);
        when(bookingService.getBookingToBooker(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(bookings);

        List<BookingDto> bookingResponseDtoList = BookingMapper.toBookingDtoList(bookings);
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", booking1.getBooker().getId())
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingResponseDtoList)));
    }

    @Test
    @DisplayName("Добавить новое бронирование")
    void addBooking() throws Exception {
        when(bookingService.addBooking(any(), anyLong(), anyLong()))
                .thenReturn(booking1);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", booking1.getBooker().getId())
                        .content(mapper.writeValueAsString(new NewBookingDto(1L, LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS), LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS))))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking1.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(booking1.getStart().format(formatter))))
                .andExpect(jsonPath("$.end", is(booking1.getEnd().format(formatter))))
                .andExpect(jsonPath("$.item.id", is(booking1.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(booking1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking1.getStatus().toString())));
    }

    @Test
    @DisplayName("Подтвердить бронирование владельцем вещи")
    void updateBooking() throws Exception {
        Booking newBooking = new Booking(booking1.getId(),
                booking1.getStart(),
                booking1.getEnd(),
                booking1.getItem(),
                booking1.getBooker(),
                Status.APPROVED);
        when(bookingService.approveBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(newBooking);

        mvc.perform(patch("/bookings/{bookingId}", booking1.getId())
                        .header("X-Sharer-User-Id", booking1.getBooker().getId())
                        .param("approved", String.valueOf(true))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking1.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(booking1.getStart().format(formatter))))
                .andExpect(jsonPath("$.end", is(booking1.getEnd().format(formatter))))
                .andExpect(jsonPath("$.item.id", is(booking1.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(booking1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(Status.APPROVED.toString())));
    }

}