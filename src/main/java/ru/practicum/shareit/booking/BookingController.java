package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingMapper;
import ru.practicum.shareit.booking.model.dto.NewBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @Valid @RequestBody NewBookingDto newBookingDto) {
        log.info("Запрос на добавление бронирования. Пользователь id " + userId + ". Вещь id "+ newBookingDto.getItemId() + ".");
        return BookingMapper.toBookingDto(
                bookingService.addBooking(
                        BookingMapper.newToBooking(newBookingDto),
                        userId,
                        newBookingDto.getItemId()
                )
        );
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestParam boolean approved) {
        log.info("Запрос на подтверждение бронирования. Пользователь id "+ userId +". Бронрование id "+ bookingId +".");
        return BookingMapper.toBookingDto(
                bookingService.approveBooking(
                        userId,
                        bookingId,
                        approved)
        );
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId) {
        log.info("Запрос бронирования id "+ bookingId +" пользователем " + userId + ".");
        return BookingMapper.toBookingDto(
                bookingService.getBooking(
                        userId,
                        bookingId)
        );
    }

    @GetMapping
    public List<BookingDto> getBookingToBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(defaultValue = "ALL") String state) {
        log.info("Запрос списка своих бронирований со статусом "+ state +" пользователем "+ userId +".");
        return BookingMapper.toBookingDtoList(
                bookingService.getBookingToBooker(
                        userId, state)
        );
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingToOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(defaultValue = "ALL") String state) {
        log.info("Запрос владельцем с id "+ userId +" списка бронирований со статусом "+ state +".");
        return BookingMapper.toBookingDtoList(
                bookingService.getBookingToOwner(
                        userId, state)
        );
    }


}
