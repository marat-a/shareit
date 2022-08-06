package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserStorageInMemory implements UserStorage{

    Map<Long,User> userMap = new HashMap<>();


    @Override
    public User addUser(User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User editUser(User user, Long id) {
        userMap.put(id, user);
        return userMap.get(id);
    }

    @Override
    public void deleteUser(Long id) {
        userMap.remove(id);
    }

    @Override
    public User getUser(Long id) {
        return userMap.get(id);
    }

    @Override
    public List<User> getAllUser() {
        return userMap.values().stream().toList();
    }
}
