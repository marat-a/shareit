package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.dto.NewBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.enums.State;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserMapper;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BookingServiceImplTest {

    private final EntityManager em;
    private final BookingService service;

    @Test
    @DisplayName("Получение бронирования вещей пользователя со state WAITING")
    void getBookingsByOwnerId() {
        //GIVEN
        //Создание пользователей
        String email1 = "eugene@mail.ru";
        String email2 = "petr@mail.ru";
        String email3 = "ooo@mail.ru";
        List<UserDto> sourceUsers = List.of(
                makeUserDto("Eugene", email1),
                makeUserDto("Petr", email2),
                makeUserDto("OOO", email3)
        );
        for (UserDto user : sourceUsers) {
            User entity = UserMapper.toUser(user);
            em.persist(entity);
        }
        em.flush();

        TypedQuery<User> queryUser = em.createQuery("select u from User u where u.email = :email", User.class);
        User owner1 = queryUser.setParameter("email", email1).getSingleResult();
        User booker = queryUser.setParameter("email", email2).getSingleResult();
        User owner2 = queryUser.setParameter("email", email3).getSingleResult();

        //Создание вещей
        Item item1 = ItemMapper.toItem(makeItemDto("Магнит", "сувенир1", true));
        item1.setOwner(owner1);
        em.persist(item1);
        Item item2 = ItemMapper.toItem(makeItemDto("Открытка", "сувенир2", true));
        item2.setOwner(owner1);
        em.persist(item2);
        Item item3 = ItemMapper.toItem(makeItemDto("Конфеты", "сувенир3", true));
        item3.setOwner(owner1);
        em.persist(item3);
        Item item4 = ItemMapper.toItem(makeItemDto("Косметика", "сувенир4", false));
        item4.setOwner(owner2);
        em.persist(item4);
        em.flush();

        TypedQuery<Item> queryItem = em.createQuery("select i from Item i where i.name = :name", Item.class);
        item1 = queryItem.setParameter("name", "Магнит").getSingleResult();
        item2 = queryItem.setParameter("name", "Открытка").getSingleResult();
        item3 = queryItem.setParameter("name", "Конфеты").getSingleResult();
        item4 = queryItem.setParameter("name", "Косметика").getSingleResult();

        //Создание бронирования
        LocalDateTime now = LocalDateTime.now();
        Booking booking1 = BookingMapper.newToBooking( new NewBookingDto(item1.getId(),now.plusDays(1), now.plusDays(2)));
        setBookingParams(booking1, booker, item1, Status.WAITING);
        em.persist(booking1);
        Booking booking2 = BookingMapper.newToBooking( new NewBookingDto(item2.getId(),now.plusDays(1), now.plusDays(2)));
        setBookingParams(booking2, booker, item2, Status.APPROVED);
        em.persist(booking2);
        Booking booking3 = BookingMapper.newToBooking( new NewBookingDto(item3.getId(),now.plusDays(1), now.plusDays(2)));
        setBookingParams(booking3, booker, item3, Status.WAITING);
        em.persist(booking3);
        Booking booking4 = BookingMapper.newToBooking( new NewBookingDto(item3.getId(),now.plusDays(1), now.plusDays(2)));
        setBookingParams(booking4, booker, item4, Status.WAITING);
        em.persist(booking4);
        em.flush();

        List<Booking> sourceBookings = List.of(booking1, booking3);

        //WHEN
        List<Booking> targetBookings = service.getBookingToOwner(owner1.getId(), State.WAITING, 0, 20);

        //THEN
        assertThat(targetBookings, hasSize(sourceBookings.size()));
        for (Booking sourceBooking : sourceBookings) {
            assertThat(targetBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(sourceBooking.getStart())),
                    hasProperty("end", equalTo(sourceBooking.getEnd())),
                    hasProperty("item", allOf(
                            hasProperty("id", equalTo(sourceBooking.getItem().getId()))
                    )),
                    hasProperty("booker", allOf(
                            hasProperty("id", equalTo(sourceBooking.getBooker().getId()))
                    )),
                    hasProperty("status", equalTo(sourceBooking.getStatus()))
            )));
        }
    }

    private void setBookingParams(Booking booking1, User booker, Item item1, Status status) {
        booking1.setBooker(booker);
        booking1.setItem(item1);
        booking1.setStatus(status);
    }

    private UserDto makeUserDto(String name, String email) {
        UserDto user = new UserDto();
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    private ItemDto makeItemDto(String name,
                                String description,
                                Boolean available) {
        ItemDto item = new ItemDto();
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(available);
        return item;
    }


}