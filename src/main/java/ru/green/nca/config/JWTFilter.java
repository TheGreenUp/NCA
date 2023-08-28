package ru.green.nca.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.green.nca.security.JWTUtil;

import java.io.IOException;
/**
 * Фильтр, который обрабатывает JWT токены для аутентификации пользователей.
 */
@AllArgsConstructor
@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * Выполняет фильтрацию для обработки JWT токенов и аутентификации пользователей.
     *
     * @param request      HTTP-запрос.
     * @param response     HTTP-ответ.
     * @param filterChain цепочка фильтров.
     * @throws ServletException Если произошла ошибка во время обработки запроса.
     * @throws IOException      Если произошла ошибка ввода-вывода.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Получение JWT из заголовка "Authorization"
        String authHeader = request.getHeader("Authorization");

        // Проверка наличия и формата токена
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            // Извлечение токена без "Bearer "
            String token = authHeader.substring(7);

            // Проверка на пустой токен
            if (token.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token in bearer header");
            } else {
                try {
                    // Валидация токена и получение имени пользователя
                    String username = jwtUtil.validateTokenAndRetrieveClaim(token);

                    // Загрузка деталей пользователя
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Создание аутентификационного токена на основе данных пользователя
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

                    // Установка аутентификации, если она еще не установлена
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                } catch (JWTVerificationException ex) {
                    // Ошибка при невалидном токене
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token");
                }
            }
        }

        // Продолжение цепочки фильтров
        filterChain.doFilter(request, response);
    }
}
