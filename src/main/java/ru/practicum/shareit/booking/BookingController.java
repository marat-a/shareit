package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.NewBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.shareit.enums.State.toState;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @Valid @RequestBody NewBookingDto newBookingDto) {
        log.info("Запрос на добавление бронирования. Пользователь id " + userId + ". Вещь id " + newBookingDto.getItemId() + ".");
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
        log.info("Запрос на подтверждение бронирования. Пользователь id " + userId + ". Бронрование id " + bookingId + ".");
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
        log.info("Запрос бронирования id " + bookingId + " пользователем " + userId + ".");
        return BookingMapper.toBookingDto(
                bookingService.getBooking(
                        userId,
                        bookingId)
        );
    }

    @GetMapping
    public List<BookingDto> getBookingToBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(defaultValue = "ALL") String state,
                                               @RequestParam (defaultValue = "0", required = false) @Min(value = 0, message = "From must be equal or more than 0")int from,
                                               @RequestParam (defaultValue = "10", required = false) @Min(value = 1, message = "Size must be more than 0") int size) {
        log.info("Запрос списка своих бронирований со статусом " + state + " пользователем " + userId + ".");
        return BookingMapper.toBookingDtoList(
                bookingService.getBookingToBooker(
                        userId, toState(state), from, size)
        );
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingToOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(defaultValue = "ALL") String state,
                                              @RequestParam (defaultValue = "0", required = false) @Min(value = 0, message = "From must be equal or more than 0")int from,
                                              @RequestParam (defaultValue = "10", required = false) @Min(value = 1, message = "Size must be more than 0") int size) {
        log.info("Запрос владельцем с id " + userId + " списка бронирований со статусом " + state + ".");
        return BookingMapper.toBookingDtoList(
                bookingService.getBookingToOwner(
                        userId, toState(state), from, size)
        );
    }


}
