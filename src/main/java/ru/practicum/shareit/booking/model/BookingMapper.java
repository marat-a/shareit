package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.NewBookingDto;

import java.util.List;
import java.util.stream.Collectors;


public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        if (booking!= null){
            return new BookingDto(
                    booking.getId(),
                    booking.getStart(),
                    booking.getEnd(),
                    booking.getBooker(),
                    booking.getBooker().getId(),
                    booking.getItem(),
                    booking.getStatus()
            );
        } else  return null;

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
