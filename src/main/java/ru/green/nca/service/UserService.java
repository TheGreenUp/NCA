package ru.green.nca.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.green.nca.entity.User;
import ru.green.nca.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor

public class UserService {
    @Autowired
    private UserRepository userRepository;
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        log.info("Find all user request");
        return users;
    }
    public ResponseEntity<User> getUserById(@PathVariable("id") int userId) {
        Optional<User> user = userRepository.findById(userId);
        log.info("Find user by id = " + userId + " request");
        return new ResponseEntity<>(user.orElse(null), HttpStatus.OK);
    }
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("Generated password for " + user.getUsername()+" is: " + user.getPassword());
        userRepository.save(user);
        log.info("Create user request with next params: " + user.toString());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    public ResponseEntity<String> deleteUser(@PathVariable("id") int userId) {
        userRepository.deleteById(userId);
        log.info("Delete user by id = " + userId + " request");
        return new ResponseEntity<>("user successfully deleted", HttpStatus.OK);
    }
    public ResponseEntity<User> updateUser(@PathVariable("id") int userId, @RequestBody User updatedUser) {
        updatedUser.setId(userId);
        User user = userRepository.save(updatedUser);
        log.info("Update user with id = " + userId + " with incoming params: " + updatedUser.toString());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
