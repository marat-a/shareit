package ru.practicum.server.booking.service;

import ru.practicum.server.booking.State;
import ru.practicum.server.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking addBooking(Booking booking, Long bookerId, Long itemId);

    Booking approveBooking(Long userId, Long bookingId, boolean approve);

    Booking getBooking(Long userId, Long bookingId);

    Booking getLastBookingByItemId(long itemId);

    Booking getNextBookingByItemId(long itemId);

    List<Booking> getBookingToBooker(Long userId, State state, int from, int size);

    List<Booking> getBookingToOwner(Long userId, State state, int from, int size);

    boolean isUserBookedItem(long itemId, long userId);

    Long getOwnerIdByBookingId(Long bookingId);
}
