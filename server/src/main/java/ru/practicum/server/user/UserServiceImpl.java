package ru.practicum.server.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.server.exceptions.NotFoundException;
import ru.practicum.server.exceptions.ValidationException;
import ru.practicum.server.user.model.User;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
            log.info("Создание пользователя " + user.getName());
            return userRepository.save(user);
    }

    @Override
    public User edit(Long id, User newUser) {
        log.info("Изменение данных пользователя c id " + id);
        User user = getUser(id);
        if (!isUserExists(id)) {
            throw new ValidationException("Пользователь с таким id  не найден");
        }
        if (newUser.getEmail() != null) {
            if (isEmailExists(newUser.getEmail())) {
                throw new ValidationException("Такой емэйл зарегистрирован");
            }
            user.setEmail(newUser.getEmail());
        }
        if (newUser.getName() != null) {
            user.setName(newUser.getName());
        }
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        log.info("Удаление пользователя "
                + userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"))
                .getName());
        userRepository.deleteById(id);
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.findAll().stream().anyMatch(i -> i.getEmail().equals(email));
    }

    @Override
    public boolean isUserExists(Long userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public User getUser(Long id) {
        log.info("Запрос данных пользователя с id " + id);
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

    }

    @Override
    public List<User> getAllUsers() {
        log.info("Запрос всех пользователей");
        return userRepository.findAll();
    }
}
