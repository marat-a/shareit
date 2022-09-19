package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository mockUserRepository;

    UserServiceImpl userServiceImpl;

    private final User user1 = new User(1L, "Евгений", "eugene@mail.ru");
    private final User user2 = new User(2L, "Петр", "petr@mail.ru");

    @BeforeEach
    void setup() {
        userServiceImpl = new UserServiceImpl(mockUserRepository);
    }



    @Test
    @DisplayName("Получить пользователя по id")
    void getUserById() {
        Long id = user1.getId();
        Mockito
                .when(mockUserRepository.findById(id))
                .thenReturn(Optional.of(user1));

        User targetUser = userServiceImpl.getUser(id);
        assertThat(targetUser.getId(), equalTo(user1.getId()));
        assertThat(targetUser.getName(), equalTo(user1.getName()));
        assertThat(targetUser.getEmail(), equalTo(user1.getEmail()));
    }

    @Test
    @DisplayName("Получить список пользователей")
    void getUsers() {
        List<User> sourceUsers = List.of(user1, user2);
        Mockito.when(mockUserRepository.findAll()).thenReturn(sourceUsers);

        List<User> targetUsers = userServiceImpl.getAllUsers();
        Assertions.assertEquals(sourceUsers.size(), targetUsers.size());
        for (User sourceUser : sourceUsers) {
            assertThat(targetUsers, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(sourceUser.getName())),
                    hasProperty("email", equalTo(sourceUser.getEmail()))
            )));
        }
    }

    @Test
    @DisplayName("Добавить пользователя")
    void addUser() {
        User newUser = new User(user1.getId(), user1.getName(), user1.getEmail());
        newUser.setId(null);
        Mockito
                .when(mockUserRepository.save(any(User.class)))
                .thenReturn(user1);

        User targetUser = userServiceImpl.create(newUser);
        assertThat(targetUser.getId(), equalTo(user1.getId()));
        assertThat(targetUser.getName(), equalTo(user1.getName()));
        assertThat(targetUser.getEmail(), equalTo(user1.getEmail()));
    }

    @Test
    @DisplayName("Обновить имя пользователя")
    void updateUserName() {
        Long id = user1.getId();
        String newName = "Eugene";
        User newUser = new User(null, newName, null);

        Mockito
                .when(mockUserRepository.findById(id))
                .thenReturn(Optional.of(user1));
        Mockito
                .when(userServiceImpl.isUserExists(id))
                .thenReturn(true);
        Mockito
                .when(mockUserRepository.save(any(User.class)))
                .thenReturn(new User(id, newName, user1.getEmail()));

        User targetUser = userServiceImpl.edit(id, newUser);
        assertThat(targetUser.getId(), equalTo(id));
        assertThat(targetUser.getName(), equalTo(newName));
        assertThat(targetUser.getEmail(), equalTo(user1.getEmail()));
    }

    @Test
    @DisplayName("Обновить имя и почту пользователя")
    void updateUser() {
        Long id = user1.getId();
        String newName = "Evgeniy";
        String newEmail = "eugene@yandex.ru";
        User newUser = new User(null, newName, newEmail);

        Mockito
                .when(mockUserRepository.findById(id))
                .thenReturn(Optional.of(user1));
        Mockito
                .when(userServiceImpl.isUserExists(id))
                .thenReturn(true);
        Mockito
                .when(mockUserRepository.save(any(User.class)))
                .thenReturn(new User(id, newName, newEmail));

        User targetUser = userServiceImpl. edit(id, newUser);
        assertThat(targetUser.getId(), equalTo(id));
        assertThat(targetUser.getName(), equalTo(newName));
        assertThat(targetUser.getEmail(), equalTo(newEmail));
    }

    @Test
    @DisplayName("Обновить почту пользователя")
    void updateUserEmail() {
        Long id = user1.getId();
        String newEmail = "eugene@yandex.ru";
        User newUser = new User(null, null, newEmail);

        Mockito
                .when(mockUserRepository.findById(id))
                .thenReturn(Optional.of(user1));
        Mockito
                .when(userServiceImpl.isUserExists(id))
                .thenReturn(true);
        Mockito
                .when(mockUserRepository.save(any(User.class)))
                .thenReturn(new User(id, user1.getName(), newEmail));

        User targetUser = userServiceImpl.edit(id, newUser);
        assertThat(targetUser.getId(), equalTo(id));
        assertThat(targetUser.getName(), equalTo(user1.getName()));
        assertThat(targetUser.getEmail(), equalTo(newEmail));
    }

    @Test
    @DisplayName("Удалить пользователя")
    void deleteUser() {
        Long id = user1.getId();

        Mockito
                .when(mockUserRepository.findById(id))
                .thenReturn(Optional.of(user1));
        userServiceImpl.delete(id);

        Mockito
                .verify(mockUserRepository, Mockito.times(1))
                .deleteById(id);
    }


}
