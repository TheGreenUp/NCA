package ru.green.nca.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
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
public class AuthController {
    private final RegistrationServiceImpl registrationService;
    private final ModelMapper modelMapper;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;


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

    public User convertToUser(UserDto userDto) {
        return this.modelMapper.map(userDto, User.class);
    }
}

