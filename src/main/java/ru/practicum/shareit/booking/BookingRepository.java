package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query ("UPDATE Booking booking SET booking.status='APPROVED' WHERE booking.id=:bookingId")
    void confirmBooking(@Param(value = "bookingId") Long bookingId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query ("UPDATE Booking booking SET booking.status='REJECTED' WHERE booking.id=:bookingId")
    void rejectBooking(@Param(value = "bookingId") Long bookingId);

    Optional<List<Booking>> findAllByBooker_IdOrderByStartDesc(long id, PageRequest pageRequest);
    Optional<List<Booking>> findAllByBooker_IdAndStartAfterOrderByStartDesc(long id, LocalDateTime startTime, PageRequest pageRequest);
    Optional<List<Booking>> findAllByBooker_IdAndEndBeforeOrderByStartDesc(long id, LocalDateTime startTime, PageRequest pageRequest);
    Optional<List<Booking>> findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(long id, LocalDateTime timeBefore, LocalDateTime timeAfter, PageRequest pageRequest);
    Optional<List<Booking>> findAllByBooker_IdAndStatusEqualsOrderByStartDesc(long id, Status status, PageRequest pageRequest);
    Optional<List<Booking>> findAllByItem_Owner_IdOrderByStartDesc(long id, PageRequest pageRequest);
    Optional<List<Booking>> findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(long id, LocalDateTime startTime, PageRequest pageRequest);
    Optional<List<Booking>> findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(long id, LocalDateTime startTime, PageRequest pageRequest);
    Optional<List<Booking>> findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(long id, LocalDateTime timeBefore, LocalDateTime timeAfter, PageRequest pageRequest);
    Optional<List<Booking>> findAllByItem_Owner_IdAndStatusEqualsOrderByStartDesc(long id, Status status, PageRequest pageRequest);

    List<Booking> findAllByBooker_IdAndItem_IdAndEndBeforeAndStatus(long id, long itemId, LocalDateTime end, Status status);

    Booking findFirstByItemIdAndEndBeforeOrderByEndDesc(Long id, LocalDateTime now);
    Booking findFirstByItemIdAndStartAfterOrderByStartAsc(Long id, LocalDateTime now);


}
