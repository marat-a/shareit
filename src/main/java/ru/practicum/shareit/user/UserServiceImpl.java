package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    UserStorage userStorage;
    Long userId;

    @Autowired
    public UserServiceImpl(@Qualifier("userStorageInMemory") UserStorage userStorage) {
        this.userStorage = userStorage;
        this.userId = 0L;
    }

    @Override
    public boolean checkIsUserEmailExists(String email){
        for (User userFromStorage : userStorage.getAllUser()){
            if (userFromStorage.getEmail().equals(email)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkUserIsExists(Long userId){
        return userStorage.getUser(userId) != null;
    }

    @Override
    public User create(User user) {
        if (checkIsUserEmailExists(user.getEmail())){
            user.setId(++userId);
            log.info("Создание пользователя " + user.getName());
            return userStorage.addUser(user);
        } else throw new ValidationException("Такой емэйл зарегистрирован");

    }

    @Override
    public User edit(Long id, User user) {
        log.info("Изменение данных пользователя c id " + id);
        if (checkUserIsExists(id)){
            if (checkIsUserEmailExists(user.getEmail())) {
                if (user.getEmail() != null) {
                    getUser(id).setEmail(user.getEmail());
                }
            } else throw new ValidationException("Такой емэйл зарегистрирован");
            if (user.getName() != null){
                getUser(id).setName(user.getName());
            }
        } else throw new ValidationException("Пользователь с таким id  не найден");
        return userStorage.editUser(getUser(id), id);
    }

    @Override
    public void delete(Long id) {
        if (checkUserIsExists(id)){
            log.info("Удаление пользователя " + userStorage.getUser(id).getName());
            userStorage.deleteUser(id);
        } else throw new ValidationException("Такой id не существует");

    }

    @Override
    public User getUser(Long id) {
        if (checkUserIsExists(id)){
            log.info("Запрос данных пользователя с id " + id );
            return userStorage.getUser(id);
        } else throw new NotFoundException("Такой id не найден");
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Запрос всех пользователей" );
        return userStorage.getAllUser();
    }
}
