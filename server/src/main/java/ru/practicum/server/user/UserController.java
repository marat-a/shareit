package ru.practicum.server.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.user.model.UserDto;
import ru.practicum.server.user.model.UserMapper;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        return UserMapper.toUserDto(userService.create(UserMapper.toUser(userDto)));
    }


    @PatchMapping("/{id}")
    public UserDto edit(@RequestBody UserDto userDto, @PathVariable Long id) {
        return UserMapper.toUserDto(userService.edit(id, UserMapper.toUser(userDto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return UserMapper.toUserDto(userService.getUser(id));
    }

    @GetMapping
    public List<UserDto> getAllUser() {
        return UserMapper.getUserDtoList(userService.getAllUsers());
    }


}
