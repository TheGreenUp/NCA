package ru.green.nca.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.green.nca.dto.AuthenticationDto;
import ru.green.nca.dto.UserDto;
import ru.green.nca.entity.User;
import ru.green.nca.security.JWTUtil;
import ru.green.nca.service.impl.RegistrationServiceImpl;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final RegistrationServiceImpl registrationService;
    private final ModelMapper modelMapper;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/registration")
    public String performRegistration(@RequestBody UserDto userDto) {
        System.out.println("Entering performReg endpoint");
        User user = convertToUser(userDto);
        registrationService.register(user);
        String token = jwtUtil.generateToken(user.getUsername());
        return token;
    }
    @PostMapping("/login")
    public String performLogin(@RequestBody AuthenticationDto authenticationDto){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationDto.getUsername(),authenticationDto.getPassword());
        try {
            authenticationManager.authenticate(authenticationToken);
        }
        catch (BadCredentialsException ex) {
            return "Incorrect credentials";
        }
        String token = jwtUtil.generateToken(authenticationDto.getUsername());
        return token;
    }

    public User convertToUser(UserDto userDto) {
        return this.modelMapper.map(userDto, User.class);
    }
}

