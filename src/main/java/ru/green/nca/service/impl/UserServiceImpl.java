package ru.green.nca.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.green.nca.entity.User;
import ru.green.nca.exceptions.NoDataFoundException;
import ru.green.nca.exceptions.ResourceNotFoundException;
import ru.green.nca.repository.UserRepository;
import ru.green.nca.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;


    @Override
    public List<User> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100));
        //TODO захардкожено, переделать бы
        Page<User> userPage = userRepository.findAll(pageable);
        if (userPage.isEmpty()) {
            log.warn("No news was found");
            throw new NoDataFoundException("No users were found");
        }
        return userPage.getContent();
    }

    @Override
    public User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " was not found"));
    }

    @Override
    public User createUser(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
        log.debug("User with id = " + userId + " was successfully deleted (maybe).");
    }

    public User updateUser(int userId, User updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // Игнорируем null значения при копировании свойств
        BeanUtils.copyProperties(updatedUser, existingUser, getNullPropertyNames(updatedUser));

        return userRepository.save(existingUser);
    }

    public String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        // Получаем свойства объекта
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        // Создаем множество для хранения имен свойств, значение которых null
        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            // Получаем значение свойства
            Object srcValue = src.getPropertyValue(pd.getName());

            // Если значение свойства равно null, добавляем имя свойства в множество
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}

