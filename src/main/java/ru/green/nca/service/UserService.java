package ru.green.nca.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.green.nca.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
}
