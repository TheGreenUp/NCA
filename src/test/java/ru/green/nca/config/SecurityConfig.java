package ru.green.nca.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.green.nca.enums.UserRole;
import ru.green.nca.service.impl.UserDetailsServiceImpl;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Конфигурационный класс для настроек Spring Security
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Profile(value = "test")
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;
    private final JWTFilter jwtFilter;

    /**
     * Настраивает фильтры безопасности и правила для различных конечных точек.
     *
     * @param http Экземпляр HttpSecurity для настройки параметров безопасности.
     * @return Настроенный SecurityFilterChain.
     * @throws Exception Если возникла ошибка во время настройки.
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .userDetailsService(userDetailsService)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest().permitAll()
                )
                .formLogin(withDefaults())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    /**
     * Предоставляет бин BCryptPasswordEncoder для кодирования паролей.
     *
     * @return Экземпляр BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Предоставляет бин AuthenticationManager для процессов аутентификации.
     *
     * @param authenticationConfiguration Конфигурация аутентификации.
     * @return Экземпляр AuthenticationManager.
     * @throws Exception Если возникла ошибка во время настройки.
     */
    @Bean
    public AuthenticationManager authenticationManager
    (AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
