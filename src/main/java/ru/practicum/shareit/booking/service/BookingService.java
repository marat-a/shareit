package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    Booking addBooking(Booking booking, Long bookerId, Long itemId);

    Booking approveBooking(Long userId, Long bookingId, boolean approve);
    boolean checkTimeHasNotPassed(LocalDateTime time);
    Long getOwnerIdByBookingId(Long bookingId);

    Booking getBooking(Long userId, Long bookingId);



    List<Booking> getBookingToBooker(Long userId, String state);

    List<Booking> getBookingToOwner(Long userId, String state);


    Booking getLastBookingByItemId(long itemId);


    Booking getNextBookingByItemId(long itemId);

    boolean isUserBookedItem(long itemId, long userId);
}
