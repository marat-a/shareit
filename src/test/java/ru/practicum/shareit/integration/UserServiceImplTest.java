package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceImplTest {

    private final EntityManager em;
    private final UserService service;

    @Test
    @DisplayName("Получение пользователя по id")
    void getUserById() {
        String email = "eugene@mail.ru";
        //GIVEN
        List<UserDto> sourceUsers = List.of(
                makeUserDto("Eugene", email),
                makeUserDto("Petr", "petr@mail.ru")
        );

        for (UserDto user : sourceUsers) {
            User entity = UserMapper.toUser(user);
            em.persist(entity);
        }
        em.flush();

        TypedQuery<User> query = em.createQuery("select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", email).getSingleResult();
        Long id = user.getId();

        //WHEN
        User targetUser = service.getUser(id);

        //THEN
        assertThat(targetUser.getId(), equalTo(user.getId()));
        assertThat(targetUser.getName(), equalTo(user.getName()));
        assertThat(targetUser.getEmail(), equalTo(user.getEmail()));
    }

    private UserDto makeUserDto(String name, String email) {
        UserDto dto = new UserDto();
        dto.setName(name);
        dto.setEmail(email);
        return dto;
    }
}