package ru.practicum.shareit.booking;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.user.model.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoJsonTest {
    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    @DisplayName("Прверить возврат бронирвания")
    void testBookingResponseDto() throws Exception {
        UserDto booker = new UserDto(1L, "Eugene", "eugene@mail.ru");
        ItemDto item = new ItemDto(1L, "Перфоратор", "перфоратор", true, null, null);

        LocalDateTime start = LocalDateTime.of(2022, 8, 29, 12, 12);
        LocalDateTime end = LocalDateTime.of(2022, 8, 30, 14, 15);

        BookingDto bookingResponseDto = new BookingDto(
                1L,
                start,
                end,
                booker,
                booker.getId(),
                item,
                Status.WAITING);

        JsonContent<BookingDto> result = json.write(bookingResponseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2022-08-29T12:12:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2022-08-30T14:15:00");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }
}
