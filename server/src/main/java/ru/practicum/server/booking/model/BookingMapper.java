package ru.practicum.server.booking.model;

import ru.practicum.server.booking.model.dto.BookingDto;
import ru.practicum.server.item.model.ItemMapper;
import ru.practicum.server.booking.model.dto.NewBookingDto;
import ru.practicum.server.user.model.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        if (booking != null) {
            return new BookingDto(
                    booking.getId(),
                    booking.getStart(),
                    booking.getEnd(),
                    UserMapper.toUserDto(booking.getBooker()),
                    booking.getBooker().getId(),
                    ItemMapper.toItemDto(booking.getItem()),
                    booking.getStatus()
            );
        } else return null;
    }

    public static Booking newToBooking(NewBookingDto newBookingDto) {
        Booking booking = new Booking();
        booking.setEnd(newBookingDto.getEnd());
        booking.setStart(newBookingDto.getStart());
        return booking;
    }

    public static List<BookingDto> toBookingDtoList(List<Booking> bookingList) {
        return bookingList.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
