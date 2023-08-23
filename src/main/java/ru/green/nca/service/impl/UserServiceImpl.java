package ru.green.nca.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.green.nca.dto.UserDto;
import ru.green.nca.entity.User;
import ru.green.nca.exceptions.NoDataFoundException;
import ru.green.nca.exceptions.ResourceNotFoundException;
import ru.green.nca.repository.UserRepository;
import ru.green.nca.security.RandomPasswordGenerator;
import ru.green.nca.security.UserDetailsImpl;
import ru.green.nca.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100));
        Page<User> userPage = userRepository.findAll(pageable);
        if (userPage.isEmpty()) {
            log.warn("No users were found");
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
    public User createUser(UserDto userDto) {
        return userRepository.save(convertToUser(userDto));
    }

    @Override
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
        log.debug("User with id = " + userId + " was successfully deleted (maybe).");
    }

    public User updateUser(int userId, UserDto updatedUserDto) {
        User updatedUser = convertToUser(updatedUserDto);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + userId + "  found"));
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

    private User convertToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        String randomPassword = RandomPasswordGenerator.generateRandomPassword();
        user.setPassword(passwordEncoder.encode(randomPassword));
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setParentName(userDto.getParentName());
        user.setRoleId(userDto.getRoleId());
        log.info("Request from userId = " + getCurrentUser().getId() + " to create/update new user." +
                " New user data:\nUsername: "+ user.getUsername()+ "\nPassword: " + randomPassword);
        return user;
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserDetailsImpl userDetails1 = (UserDetailsImpl) userDetails;
        return userDetails1.getUser();
    }
}

