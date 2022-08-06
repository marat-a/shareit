package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User editUser(User user, Long id);

    void deleteUser(Long id);

    User getUser(Long id);

    List<User> getAllUser();
}
