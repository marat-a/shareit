package ru.practicum.server.user;


import ru.practicum.server.user.model.User;

import java.util.List;

public interface UserService {
    boolean isEmailExists(String email);

    boolean isUserExists(Long userId);

    User create(User user);

    User edit(Long id, User user);

    void delete(Long id);

    User getUser(Long id);

    List<User> getAllUsers();
}
