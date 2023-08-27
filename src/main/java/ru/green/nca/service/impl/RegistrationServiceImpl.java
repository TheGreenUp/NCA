package ru.green.nca.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.green.nca.entity.User;
import ru.green.nca.repository.UserRepository;


/**
 * Реализация сервиса для регистрации пользователей.
 */
@Service
@AllArgsConstructor
@Profile(value = "default")
public class RegistrationServiceImpl {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    /**
     * Регистрация нового пользователя в системе.
     *
     * @param user объект пользователя для регистрации
     */
    public void register(User user) {
        // Хеширование пароля перед сохранением в базу данных
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    /**
     * Проверка существования пользователя с заданным именем пользователя.
     *
     * @param user объект пользователя, содержащий имя пользователя
     * @return {@code true}, если пользователь с указанным именем уже существует, иначе {@code false}
     */
    public boolean checkExistence(User user) {
        return userRepository.findByUsername(user.getUsername()).isPresent();
    }
}