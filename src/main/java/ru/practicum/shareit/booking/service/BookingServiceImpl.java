package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserService userService;
    private final ItemService itemService;
    private final BookingRepository bookingRepository;

    @Override
    public Booking addBooking(Booking booking, Long bookerId, Long itemId) {
        if (!userService.isUserExists(bookerId) || !itemService.isItemExist(itemId)) {
            throw new NotFoundException("Не найден пользователь или вещь с таким id");
        } else if (itemService.getItem(itemId).getAvailable()
                && checkTimeHasNotPassed(booking.getStart())
                && checkTimeHasNotPassed(booking.getEnd())
        ) {
            booking.setBooker(userService.getUser(bookerId));
            booking.setItem(itemService.getItem(itemId));
            booking.setStatus(Status.WAITING);
            return bookingRepository.save(booking);
        } else throw new BadRequestException("Некорреткные данные");
    }

    @Override
    public Booking approveBooking(Long userId, Long bookingId, boolean approve) {
        if (userId.equals(getOwnerIdByBookingId(bookingId))) {
            if (approve) {
                bookingRepository.confirmBooking(bookingId);
            } else bookingRepository.rejectBooking(bookingId);
        }
        return getBookingById(bookingId);
    }

    @Override
    public boolean checkTimeHasNotPassed(LocalDateTime time) {
        return time.isAfter(LocalDateTime.now());
    }

    @Override
    public Long getOwnerIdByBookingId(Long bookingId) {
        return getBookingById(bookingId).getItem().getOwner().getId();
    }

    @Override
    public Booking getBooking(Long userId, Long bookingId) {
        if (getOwnerIdByBookingId(bookingId).equals(userId)
                || getBookingById(bookingId).getBooker().getId().equals(userId)){
            return getBookingById(bookingId);
        } else throw new NotFoundException("Информация о бронировании недоступна");

    }

    Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(()-> new NotFoundException("Бронирование не найдено"));
    }
}
