package ru.green.nca.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.green.nca.repository.UserRepository;
import ru.green.nca.security.UserDetailsImpl;

/**
 * Реализация интерфейса UserDetailsService для аутентификации пользователей.
 */
@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Загрузка данных пользователя по имени пользователя для аутентификации.
     *
     * @param username имя пользователя
     * @return объект UserDetails, представляющий данные пользователя
     * @throws UsernameNotFoundException если пользователь с указанным именем не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("User" + username + " not found!"));
    }
}
