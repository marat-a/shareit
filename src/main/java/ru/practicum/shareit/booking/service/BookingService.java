package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking addBooking(Booking booking, Long bookerId, Long itemId);

    Booking approveBooking(Long userId, Long bookingId, boolean approve);

    Booking getBooking(Long userId, Long bookingId);

    Booking getLastBookingByItemId(long itemId);

    Booking getNextBookingByItemId(long itemId);

    List<Booking> getBookingToBooker(Long userId, String state);

    List<Booking> getBookingToOwner(Long userId, String state);

    boolean isUserBookedItem(long itemId, long userId);

    Long getOwnerIdByBookingId(Long bookingId);
}
