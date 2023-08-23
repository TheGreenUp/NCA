package ru.green.nca.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.green.nca.entity.User;
import ru.green.nca.repository.UserRepository;
import ru.green.nca.security.UserDetailsImpl;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<User> user = userRepository.findByUsername(username);
       if (user.isEmpty()) throw new UsernameNotFoundException("User not found!");
       return new UserDetailsImpl(user.get());
    }
}
