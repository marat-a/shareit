package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;

public interface BookingService {
    Booking addBooking(Booking booking, Long bookerId, Long itemId);

    Booking approveBooking(Long userId, Long bookingId, boolean approve);
    boolean checkTimeHasNotPassed(LocalDateTime time);
    Long getOwnerIdByBookingId(Long bookingId);

    Booking getBooking(Long userId, Long bookingId);

}
