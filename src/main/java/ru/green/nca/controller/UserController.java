package ru.green.nca.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.green.nca.dto.UserDto;
import ru.green.nca.entity.User;
import ru.green.nca.service.UserService;

import java.util.List;
/**
 * Контроллер для обработки REST API запросов, связанных с сущностью "User".
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;
    /**
     * Получение списка пользователей с пагинацией.
     *
     * @param page номер страницы (по умолчанию: 0)
     * @param size количество пользователей на странице (по умолчанию: 10)
     * @return список пользователей
     */
    @GetMapping
    public List<User> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        log.info("Entering 'get all users' endpoint");
        return userService.getAllUsers(page, size);
    }
    /**
     * Получение пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return найденный пользователь
     */
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") int userId) {
        log.info("Entering 'get user by id' endpoint");
        return userService.getUserById(userId);
    }
    /**
     * Создание нового пользователя.
     *
     * @param userDto данные пользователя для создания
     * @return созданный пользователь
     */
    @PostMapping
    public User createUser(@RequestBody UserDto userDto) {
        log.info("Entering 'create user' endpoint");
        return userService.createUser(userDto);
    }

    /**
     * Удаление пользователя по идентификатору.
     *
     * @param userId идентификатор пользователя для удаления
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") int userId) {
        log.info("Entering 'delete user' endpoint");
        userService.deleteUser(userId);
    }
    /**
     * Обновление информации о пользователе.
     *
     * @param userId        идентификатор пользователя для обновления
     * @param updatedUserDto обновленные данные пользователя
     * @return обновленный пользователь
     */
    @PutMapping("/{id}")
    public User updateUser(@PathVariable("id") int userId, @RequestBody UserDto updatedUserDto) {
        log.info("Entering 'update user' endpoint");
        updatedUserDto.setId(userId);
        return userService.updateUser(userId, updatedUserDto);
    }

}
