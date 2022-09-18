package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.enums.State;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    private final User user1 = new User(1L, "Eugene", "eugene@mail.ru");
    private final User user2 = new User(2L, "Petr", "petr@mail.ru");
    private final Item item1 = new Item(1L, user1, "Магнит", "сувенир", true, null);
    private final Booking booking1 = new Booking(1L,
            LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS), LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS), item1, user2, Status.WAITING);
    private final Item item2 = new Item(2L, user1, "Открытка", "сувенир", true, null);
    private final Booking booking2 = new Booking(2L,
            LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS), LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS), item2, user2, Status.WAITING);

    @Mock
    BookingRepository mockBookingRepository;
    @Mock
    ItemService mockItemService;
    @Mock
    UserService mockUserService;
    BookingServiceImpl bookingServiceImpl;

    @BeforeEach
    void setUp() {
        bookingServiceImpl = new BookingServiceImpl(mockUserService, mockItemService, mockBookingRepository);
    }

    @Test
    @DisplayName("Просмотр бронирования")
    void getBookingById() {
        Mockito
                .when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking1));

        Booking targetBooking = bookingServiceImpl.getBooking(user1.getId(), booking1.getId());
        checkFields(targetBooking, booking1);
    }

    @Test
    @DisplayName("Добавление бронирования")
    void addBooking() {
        Mockito
                .when(mockItemService.isItemExist(anyLong()))
                .thenReturn(true);
        Mockito
                .when(mockUserService.isUserExists(anyLong()))
                .thenReturn(true);
        Mockito
                .when(mockItemService.getItem(anyLong()))
                .thenReturn(item1);
        Mockito
                .when(mockUserService.getUser(anyLong()))
                .thenReturn(user2);
        Mockito
                .when(mockBookingRepository.save(any()))
                .thenReturn(booking1);
        Booking targetBooking = bookingServiceImpl.addBooking(booking1, user2.getId(), item1.getId());
        checkFields(targetBooking, booking1);
    }

    @Test
    @DisplayName("Проставление статуса бронирования")
    void updateBookingStatus() {
        Booking sourceBooking = copyBooking(booking1);
        sourceBooking.setStatus(Status.APPROVED);

        Mockito
                .when(mockBookingRepository.findById(anyLong())).thenAnswer(new Answer() {
                    private int count = 0;

                    public Object answer(InvocationOnMock invocation) {
                        if (count++ == 1)
                            return Optional.of(booking1);

                        return  Optional.of(sourceBooking);
                    }
                });
        Booking targetBooking = bookingServiceImpl.approveBooking(user1.getId(), booking1.getId(), true);
        checkFields(targetBooking, sourceBooking);
    }

    @Test
    @DisplayName("Получение бронирования для пользователя со state WAITING")
    void getBookingsByBookerId() {
        List<Booking> sourceBookings = List.of(booking1, booking2);
        Mockito
                .when(mockUserService.isUserExists(anyLong()))
                .thenReturn(true);
        Mockito
                .when(mockBookingRepository.findAllByBooker_IdAndStatusEqualsOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(Optional.of(sourceBookings));

        List<Booking> targetBookings = bookingServiceImpl.getBookingToBooker(user1.getId(), State.WAITING, 0, 20);
        Assertions.assertEquals(sourceBookings.size(), targetBookings.size());
    }

    @Test
    @DisplayName("Получение бронирования вещей пользователя со state WAITING")
    void getBookingsByOwnerId() {
        List<Booking> sourceBookings = List.of(booking1);
        Mockito
                .when(mockUserService.isUserExists(anyLong()))
                .thenReturn(true);

        Mockito
                .when(mockBookingRepository.findAllByItem_Owner_IdAndStatusEqualsOrderByStartDesc(anyLong(), any(), any()))
                .thenAnswer(invocationOnMock -> {
                    long itemId = invocationOnMock.getArgument(0, Long.class);
                    if (itemId == 1) {
                        return Optional.of(List.of(booking1));
                    } else if (itemId == 2) {
                        return Optional.of(List.of(booking2));
                    }
                    return null;
                });

        List<Booking> targetBookings = bookingServiceImpl.getBookingToOwner(user1.getId(), State.WAITING, 0, 20);
        Assertions.assertEquals(sourceBookings.size(), targetBookings.size());
    }

    @Test
    @DisplayName("Получение последнего бронирования вещи")
    void getLastBookingByItemId() {
        Mockito
                .when(mockBookingRepository.findFirstByItemIdAndEndBeforeOrderByEndDesc(anyLong(), any()))
                .thenReturn(booking1);

        Booking targetBooking = bookingServiceImpl.getLastBookingByItemId(item1.getId());
        checkFields(targetBooking, booking1);
    }

    @Test
    @DisplayName("Получение ближайшего бронирования вещи")
    void getNextBookingByItemId() {
        Mockito
                .when(mockBookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(anyLong(), any()))
                .thenReturn(booking1);

        Booking targetBooking = bookingServiceImpl.getNextBookingByItemId(item1.getId());
        checkFields(targetBooking, booking1);
    }

    private void checkFields(Booking targetBooking, Booking sourceBooking) {
        assertThat(targetBooking.getId(), equalTo(sourceBooking.getId()));
        assertThat(targetBooking.getStart(), equalTo(sourceBooking.getStart()));
        assertThat(targetBooking.getEnd(), equalTo(sourceBooking.getEnd()));
        assertThat(targetBooking.getItem(), equalTo(sourceBooking.getItem()));
        assertThat(targetBooking.getBooker(), equalTo(sourceBooking.getBooker()));
        assertThat(targetBooking.getStatus(), equalTo(sourceBooking.getStatus()));
    }

    private Booking copyBooking(Booking booking) {
        Booking newBooking = new Booking();
        newBooking.setId(booking.getId());
        newBooking.setStart(booking.getStart());
        newBooking.setEnd(booking.getEnd());
        newBooking.setItem(booking.getItem());
        newBooking.setBooker(booking.getBooker());
        newBooking.setStatus(booking.getStatus());
        return newBooking;
    }
}