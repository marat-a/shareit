package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table (name="bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="booking_id")
    private Long id;
    @Column(name="start_time")
    private LocalDate start;
    @Column(name="end_time")
    private LocalDate end;
    @ManyToOne
    @JoinColumn (name="item")
    private Item item;
    @ManyToOne
    @JoinColumn (name="booker")
    private User booker;
    @Enumerated(EnumType.STRING)
    private Status status;

}
