package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;

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
        if (isEmailExists(user.getEmail())) {
            log.info("Создание пользователя " + user.getName());
            return userRepository.save(user);
        } else throw new ValidationException("Такой емэйл зарегистрирован");

    }

    @Override
    public User edit(Long id, User newUser) {
        log.info("Изменение данных пользователя c id " + id);
        User user = getUser(id);
        if (isUserExists(id)) {

            if (isEmailExists(newUser.getEmail())) {
                if (newUser.getEmail() != null) {
                    user.setEmail(newUser.getEmail());
                }
            } else throw new ValidationException("Такой емэйл зарегистрирован");
            if (newUser.getName() != null) {
                user.setName(newUser.getName());
            }
        } else throw new ValidationException("Пользователь с таким id  не найден");
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
        return userRepository.findAll().stream().noneMatch(i -> i.getEmail().equals(email));
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
