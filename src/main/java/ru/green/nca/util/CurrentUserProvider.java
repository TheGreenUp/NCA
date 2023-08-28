package ru.green.nca.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ru.green.nca.entity.User;
import ru.green.nca.security.UserDetailsImpl;

public class CurrentUserProvider {
    private CurrentUserProvider(){

    }
    private static final class InstanceHolder {
        private static final CurrentUserProvider instance = new CurrentUserProvider();
    }

    public static CurrentUserProvider getInstance() {
        return InstanceHolder.instance;
    }
    /**
     * Получение текущего пользователя.
     *
     * @return объект пользователя, представляющий текущего аутентифицированного пользователя
     */
    public User getCurrentUser() {
        //Получаем текущий объект аутентификации из контекста безопасности
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //получаем userDetails, связанные с аутентификацией
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserDetailsImpl userData = (UserDetailsImpl) userDetails;
        return userData.getUser();
    }
}
