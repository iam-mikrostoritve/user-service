package org.iammikrostoritve.userservice.controller;

import org.iammikrostoritve.userservice.dto.LoginUserDto;
import org.iammikrostoritve.userservice.dto.SignUpUserDto;
import org.iammikrostoritve.userservice.model.User;
import org.iammikrostoritve.userservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDto loginUser) {
        logger.info("Called endpoint: POST /api/users/login");
        Optional<User> user = userService.login(loginUser);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password not found");
        }
    }

    @PostMapping("/signUp")
    public User signUp(@RequestBody SignUpUserDto user) {
        logger.info("Called endpoint: POST /api/users/signUp");
        return userService.createUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        logger.info("Called endpoint: GET /api/users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        logger.info("Called endpoint: GET /api/users/{}", id);
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        logger.info("Called endpoint: PUT /api/users/{}", id);
        Optional<User> user = userService.updateUser(id, updatedUser);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        logger.info("Called endpoint: DELETE /api/users/{}", id);
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}