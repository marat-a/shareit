package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query ("UPDATE Booking booking SET booking.status='APPROVED' WHERE booking.id=:bookingId")
    void confirmBooking(@Param(value = "bookingId") Long bookingId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query ("UPDATE Booking booking SET booking.status='REJECTED' WHERE booking.id=:bookingId")
    void rejectBooking(@Param(value = "bookingId") Long bookingId);
}
