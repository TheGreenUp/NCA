package ru.green.nca.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.green.nca.service.impl.UserDetailsServiceImpl;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig{
    private final UserDetailsServiceImpl userDetailsService;
    private final JWTFilter jwtFilter;
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .userDetailsService(userDetailsService)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(HttpMethod.GET, "/api/news/**", "/api/comments/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()

                                .requestMatchers(HttpMethod.POST, "/api/comments.**").hasRole("SUBSCRIBER")
                                .requestMatchers(HttpMethod.PUT, "/api/comments.**").hasRole("SUBSCRIBER")

                                .requestMatchers(HttpMethod.POST,"/api/news/, /api/comments.**").hasRole("JOURNALIST")
                                .requestMatchers(HttpMethod.POST,"/api/news/, /api/comments.**").hasRole("JOURNALIST")

                                .anyRequest().hasRole("ADMIN")
                )
                .formLogin(withDefaults())
                .sessionManagement(sessionManagment ->sessionManagment.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
