package ru.green.nca.service;

import ru.green.nca.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers(int page, int size);

    UserDto getUserById(int userId);

    UserDto createUser(UserDto userDto);

    void deleteUser(int userId);

    UserDto updateUser(int userId, UserDto updatedUserDto);

    String[] getNullPropertyNames(Object source);
}