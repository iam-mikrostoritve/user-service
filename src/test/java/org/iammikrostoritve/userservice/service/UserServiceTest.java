package org.iammikrostoritve.userservice.service;

import org.iammikrostoritve.userservice.dto.LoginUserDto;
import org.iammikrostoritve.userservice.dto.SignUpUserDto;
import org.iammikrostoritve.userservice.model.User;
import org.iammikrostoritve.userservice.model.UserRole;
import org.iammikrostoritve.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        LoginUserDto loginUser = new LoginUserDto("test@test.com", "password");
        User user = new User("Test", "User", "test@test.com", passwordEncoder.encode("password"), UserRole.USER);
        when(userRepository.findByEmail(loginUser.getEmail())).thenReturn(Optional.of(user));

        Optional<User> result = userService.login(loginUser);

        assertTrue(result.isPresent());
        assertEquals(user.getEmail(), result.get().getEmail());
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User("Test", "User", "test1@test.com", passwordEncoder.encode("password"), UserRole.USER);
        User user2 = new User("Test", "User", "test2@test.com", passwordEncoder.encode("password"), UserRole.USER);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
    }

    @Test
    void testGetUserById() {
        String id = "1";
        User user = new User("Test", "User", "test@test.com", passwordEncoder.encode("password"), UserRole.USER);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(id);

        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
    }

    @Test
    void testCreateUser() {
        SignUpUserDto signUpUserDto = new SignUpUserDto("Test", "User", "test@test.com", "password", UserRole.USER);
        User user = new User("Test", "User", "test@test.com", passwordEncoder.encode("password"), UserRole.USER);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser(signUpUserDto);

        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void testUpdateUser() {
        String id = "1";
        User updatedUser = new User("Updated", "User", "updated@test.com", passwordEncoder.encode("password"), UserRole.USER);
        when(userRepository.findById(id)).thenReturn(Optional.of(updatedUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        Optional<User> result = userService.updateUser(id, updatedUser);

        assertTrue(result.isPresent());
        assertEquals(updatedUser.getEmail(), result.get().getEmail());
    }

    @Test
    void testDeleteUser() {
        String id = "1";
        when(userRepository.existsById(id)).thenReturn(true);

        boolean result = userService.deleteUser(id);

        assertTrue(result);
    }
}