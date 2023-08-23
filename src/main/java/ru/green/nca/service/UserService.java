package ru.green.nca.service;

import ru.green.nca.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers(int page, int size);

    User getUserById(int userId);

    User createUser(User user);

    void deleteUser(int userId);

    User updateUser(int userId, User updatedUser);

    String[] getNullPropertyNames(Object source);
}