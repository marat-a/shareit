package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table (name="bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate start;
    private LocalDate end;
    @OneToOne
    private Item item;
    @OneToOne
    private User booker;
    @Enumerated(EnumType.STRING)
    private Status status;

}
