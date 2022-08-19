package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {



//    public BookingDTO addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
//                           @Valid @RequestBody BookingDTO bookingDTO) {
//        return ItemMapper.toBookingDto(bookingService.addBooking(userId, ItemMapper.toItem(itemDto, userService.getUser(userId))));
//    }
}
