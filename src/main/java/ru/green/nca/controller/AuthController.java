package ru.green.nca.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.green.nca.dto.AuthenticationDto;
import ru.green.nca.dto.UserDto;
import ru.green.nca.entity.User;
import ru.green.nca.exceptions.ConflictException;
import ru.green.nca.security.JWTUtil;
import ru.green.nca.security.RandomPasswordGenerator;
import ru.green.nca.service.impl.RegistrationServiceImpl;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
@Profile(value = "default")
public class AuthController {
    private final RegistrationServiceImpl registrationService;
    private final ModelMapper modelMapper;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрирует нового пользователя.
     *
     * @param userDto DTO с информацией о пользователе.
     * @return Map с JWT-токеном и рандомно сгенерированным паролем.
     * @throws ConflictException если пользователь с таким именем пользователя уже существует.
     */
    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody UserDto userDto) {
        log.info("Entering performReg endpoint");
        User user = convertToUser(userDto);

        if (registrationService.checkExistence(user))
            throw new ConflictException("User with username = " + user.getUsername() + " already exist in database");

        String randomPassword = RandomPasswordGenerator.generateRandomPassword();
        user.setPassword(randomPassword);
        registrationService.register(user);
        log.debug("Username: " + user.getUsername()  + ", password = " + randomPassword + " was successfully registered");
        String token = jwtUtil.generateToken(user.getUsername());
        return Map.of("jwt-token: ", token, "password: ", randomPassword);
    }

    /**
     * Выполняет вход пользователя.
     *
     * @param authenticationDto DTO с информацией для аутентификации.
     * @return Map с JWT-токеном в случае успешной аутентификации или ошибкой в случае неверных учетных данных.
     */

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDto authenticationDto) {
        // Создаем аутентификационный токен на основе данных пользователя
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationDto.getUsername(),
                        authenticationDto.getPassword());
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException ex) {
            return Map.of("Error: ", "bad credentials");
        }
        String token = jwtUtil.generateToken(authenticationDto.getUsername());
        return Map.of("jwt-token: ", token);
    }
    /**
     * Преобразует UserDto в сущность User.
     *
     * @param userDto DTO с информацией о пользователе.
     * @return Объект User, полученный в результате преобразования.
     */
    public User convertToUser(UserDto userDto) {
        return this.modelMapper.map(userDto, User.class);
    }
}

