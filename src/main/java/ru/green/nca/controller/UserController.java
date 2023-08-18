package ru.green.nca.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.green.nca.entity.User;
import ru.green.nca.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        log.info("Find all user request");
        return users;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getuserById(@PathVariable("id") int userId) {
        Optional<User> user = userRepository.findById(userId);
        log.info("Find user by id = " + userId + " request");
        return new ResponseEntity<>(user.orElse(null), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createuser(@RequestBody User user) {
        log.info("Generated password for " + user.getUsername()+" is: " + user.getPassword());
        userRepository.save(user);
        log.info("Create user request with next params: " + user.toString());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int userId) {
        userRepository.deleteById(userId);
        log.info("Delete user by id = " + userId + " request");
        return new ResponseEntity<>("user successfully deleted", HttpStatus.OK);
    }
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") int userId, @RequestBody User updatedUser) {
        updatedUser.setId(userId);
        User user = userRepository.save(updatedUser);
        log.info("Update user with id = " + userId + " with incoming params: " + updatedUser.toString());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
