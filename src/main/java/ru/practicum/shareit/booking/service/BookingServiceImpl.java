package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.State;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.StateException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private UserService userService;
    private ItemService itemService;
    private BookingRepository bookingRepository;

    @Override
    public Booking addBooking(Booking booking, Long bookerId, Long itemId) {
        if (bookerId.equals(itemService.getItem(itemId).getOwner().getId())) {
            log.info("Нельзя арендовать свою вещь");
            throw new NotFoundException("Нельзя арендовать свою вещь");
        }
        if (!userService.isUserExists(bookerId) || !itemService.isItemExist(itemId)) {
            log.info("Не найден пользователь или вещь с таким id");
            throw new NotFoundException("Не найден пользователь или вещь с таким id");
        }
        if (!itemService.getItem(itemId).getAvailable()) {
            log.info("Вещь недоступна для аренды");
            throw new BadRequestException("Вещь недоступна для аренды");
        }
        if (booking.getStart().isBefore(LocalDateTime.now()) || booking.getEnd().isBefore(booking.getStart())) {
            log.info("Указано неправильное время аренды");
            throw new BadRequestException("Указано неправильное время аренды");
        }
        booking.setBooker(userService.getUser(bookerId));
        booking.setItem(itemService.getItem(itemId));
        booking.setStatus(Status.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking approveBooking(Long userId, Long bookingId, boolean approve) {
        if (!userId.equals(getOwnerIdByBookingId(bookingId))) {
            throw new NotFoundException("Пользователь не является владельцем вещи");
        }
        if (!getBookingById(bookingId).getStatus().equals(Status.WAITING)) {
            throw new BadRequestException("Статус бронирования не WAITING");
        }
        if (approve) {
            bookingRepository.confirmBooking(bookingId);
        } else bookingRepository.rejectBooking(bookingId);
        return getBookingById(bookingId);
    }

    @Override
    public Long getOwnerIdByBookingId(Long bookingId) {
        return getBookingById(bookingId).getItem().getOwner().getId();
    }

    @Override
    public Booking getBooking(Long userId, Long bookingId) {
        if (getOwnerIdByBookingId(bookingId).equals(userId) || getBookingById(bookingId).getBooker().getId().equals(userId)) {
            return getBookingById(bookingId);
        } else throw new NotFoundException("Информация о бронировании недоступна данному пользователю");
    }

    @Override
    public List<Booking> getBookingToBooker(Long userId, State state) {
        if (userService.isUserExists(userId)) {
            switch (state) {
                case ALL:
                    return bookingRepository.findAllByBooker_IdOrderByStartDesc(userId).orElseThrow(() -> new NotFoundException("Бронирований не найдено"));
                case FUTURE:
                    return bookingRepository.findAllByBooker_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now()).orElseThrow(() -> new NotFoundException("Бронирований FUTURE не найдено"));
                case CURRENT:
                    return bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now()).orElseThrow(() -> new NotFoundException("Бронирований CURRENT не найдено"));
                case PAST:
                    return bookingRepository.findAllByBooker_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now()).orElseThrow(() -> new NotFoundException("Бронирований PAST не найдено"));
                case REJECTED:
                case WAITING:
                    return bookingRepository.findAllByBooker_IdAndStatusEqualsOrderByStartDesc(userId, Status.valueOf(state.toString())).orElseThrow(() -> new NotFoundException("Бронирований REJECTED или WAITING не найдено"));
                default:
                    throw new StateException("Unknown state: " + state);
            }
        } else throw new NotFoundException("Пользователь не найден");
    }

    @Override
    public List<Booking> getBookingToOwner(Long userId, State state) {
        if (userService.isUserExists(userId)) {
            switch (state) {
                case ALL:
                    return bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(userId).orElseThrow(() -> new NotFoundException("Бронирований не найдено"));
                case FUTURE:
                    return bookingRepository.findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now()).orElseThrow(() -> new NotFoundException("Бронирований FUTURE не найдено"));
                case CURRENT:
                    return bookingRepository.findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now()).orElseThrow(() -> new NotFoundException("Бронирований CURRENT не найдено"));
                case PAST:
                    return bookingRepository.findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now()).orElseThrow(() -> new NotFoundException("Бронирований PAST не найдено"));
                case REJECTED:
                case WAITING:
                    return bookingRepository.findAllByItem_Owner_IdAndStatusEqualsOrderByStartDesc(userId, Status.valueOf(state.toString())).orElseThrow(() -> new NotFoundException("Бронирований REJECTED или WAITING не найдено"));
                default:
                    throw new StateException("Unknown state: " + state);
            }
        } else throw new NotFoundException("Пользователь не найден");
    }

    Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
    }

    @Override
    public Booking getLastBookingByItemId(long itemId) {
        return bookingRepository.findFirstByItemIdAndEndBeforeOrderByEndDesc(itemId, LocalDateTime.now());
    }

    @Override
    public Booking getNextBookingByItemId(long itemId) {
        return bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(itemId, LocalDateTime.now());
    }

    @Override
    public boolean isUserBookedItem(long itemId, long userId) {
        List<Booking> bookings = bookingRepository.findAllByBooker_IdAndItem_IdAndEndBeforeAndStatus(userId, itemId, LocalDateTime.now(), Status.APPROVED);
        return !bookings.isEmpty();
    }
}
