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
 * Configuration class for Spring Security settings and filters.
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Profile(value = "default")
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;
    private final JWTFilter jwtFilter;
    /**
     * Configures the security filters and rules for different endpoints.
     *
     * @param http HttpSecurity instance for configuring security settings.
     * @return The configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .userDetailsService(userDetailsService)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests

                                .requestMatchers(HttpMethod.GET,  "/api/news/**", "/api/comments/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                                .requestMatchers("/api/news/**").hasAnyRole(UserRole.JOURNALIST.name(), UserRole.ADMIN.name())

                                .requestMatchers("/api/comments/**").hasAnyRole(UserRole.SUBSCRIBER.name(), UserRole.ADMIN.name())

                                .anyRequest().hasRole(UserRole.ADMIN.name())
                )
                .formLogin(withDefaults())
                .sessionManagement(sessionManagment -> sessionManagment.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    /**
     * Provides a BCryptPasswordEncoder bean for password encoding.
     *
     * @return BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /**
     * Provides an AuthenticationManager bean for authentication processes.
     *
     * @param authenticationConfiguration The authentication configuration.
     * @return AuthenticationManager instance.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
