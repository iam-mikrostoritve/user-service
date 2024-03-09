package org.iammikrostoritve.userservice.service;

import org.iammikrostoritve.userservice.dto.LoginUserDto;
import org.iammikrostoritve.userservice.dto.SignUpUserDto;
import org.iammikrostoritve.userservice.model.User;
import org.iammikrostoritve.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> login(LoginUserDto loginUser) {
        Optional<User> optionalUser = userRepository.findByEmail(loginUser.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(loginUser.getPassword(), user.getHashedPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public User createUser(SignUpUserDto user) {
        User newUser = new User(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getRole());
        newUser.setHashedPassword(new BCryptPasswordEncoder().encode(newUser.getHashedPassword()));
        return userRepository.save(newUser);
    }

    public Optional<User> updateUser(String id, User updatedUser) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setHashedPassword(updatedUser.getHashedPassword());
            existingUser.setRole(updatedUser.getRole());
            return Optional.of(userRepository.save(existingUser));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }


}