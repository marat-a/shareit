package ru.practicum.shareit.booking.model.dto;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.stream.Collectors;


public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getBooker(),
                booking.getItem(),
                booking.getStatus()
        );
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
