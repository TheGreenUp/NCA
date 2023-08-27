package ru.green.nca.service;

import ru.green.nca.dto.UserDto;
import ru.green.nca.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers(int page, int size);

    UserDto getUserById(int userId);

    User createUser(UserDto userDto);

    void deleteUser(int userId);

    User updateUser(int userId, UserDto updatedUserDto);

    String[] getNullPropertyNames(Object source);
}