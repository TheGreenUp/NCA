package ru.green.nca.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.green.nca.dto.UserDto;
import ru.green.nca.entity.User;
import ru.green.nca.service.UserService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        log.info("Entering 'get all users' endpoint");
        return userService.getAllUsers(page, size);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") int userId) {
        log.info("Entering 'get user by id' endpoint");
        return userService.getUserById(userId);
    }

    @PostMapping
    public User createUser(@RequestBody UserDto userDto) {
        log.info("Entering 'create user' endpoint");
        return userService.createUser(convertToUser(userDto));
    }


    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") int userId) {
        log.info("Entering 'delete user' endpoint");
        userService.deleteUser(userId);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable("id") int userId, @RequestBody UserDto updatedUserDto) {
        updatedUserDto.setId(userId);
        log.info("Entering 'update user' endpoint");
        return userService.updateUser(userId, convertToUser(updatedUserDto));
    }
    private User convertToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        //TODO automatic password set
        user.setPassword(userDto.getPassword());
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setParentName(userDto.getParentName());
        //TODO с этим тоже разобраться нужно
        user.setRoleId(1);
        return user;
    }
}
