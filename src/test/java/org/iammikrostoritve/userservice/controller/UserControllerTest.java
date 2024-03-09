package org.iammikrostoritve.userservice.controller;

import org.iammikrostoritve.userservice.dto.LoginUserDto;
import org.iammikrostoritve.userservice.dto.SignUpUserDto;
import org.iammikrostoritve.userservice.model.User;
import org.iammikrostoritve.userservice.model.UserRole;
import org.iammikrostoritve.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testLogin() throws Exception {
        LoginUserDto loginUser = new LoginUserDto("test@test.com", "password");
        User user = new User("Test", "User", "test@test.com", "password", UserRole.USER);
        when(userService.login(loginUser)).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@test.com\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"firstName\":\"Test\", \"lastName\":\"User\", \"email\":\"test@test.com\", \"role\":\"USER\"}"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        User user1 = new User("Test", "User", "test1@test.com", "password", UserRole.USER);
        User user2 = new User("Test", "User", "test2@test.com", "password", UserRole.USER);
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"firstName\":\"Test\", \"lastName\":\"User\", \"email\":\"test1@test.com\", \"role\":\"USER\"}, {\"firstName\":\"Test\", \"lastName\":\"User\", \"email\":\"test2@test.com\", \"role\":\"USER\"}]"));
    }

    @Test
    void testSignUp() throws Exception {
        SignUpUserDto signUpUserDto = new SignUpUserDto("Test", "User", "test@test.com", "password", UserRole.USER);
        User user = new User("Test", "User", "test@test.com", passwordEncoder.encode("password"), UserRole.USER);
        when(userService.createUser(signUpUserDto)).thenReturn(user);

        mockMvc.perform(post("/api/users/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Test\", \"lastName\":\"User\", \"email\":\"test@test.com\", \"role\":\"USER\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserById() throws Exception {
        String id = "1";
        User user = new User("Test", "User", "test@test.com", passwordEncoder.encode("password"), UserRole.USER);
        when(userService.getUserById(id)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"firstName\":\"Test\", \"lastName\":\"User\", \"email\":\"test@test.com\", \"role\":\"USER\"}"));
    }

    @Test
    void testUpdateUser() throws Exception {
        String id = "1";
        User updatedUser = new User("Updated", "User", "updated@test.com", passwordEncoder.encode("password"), UserRole.USER);
        when(userService.updateUser(anyString(), any(User.class))).thenReturn(Optional.of(updatedUser));

        mockMvc.perform(put("/api/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Updated\", \"lastName\":\"User\", \"email\":\"updated@test.com\", \"role\":\"USER\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"firstName\":\"Updated\", \"lastName\":\"User\", \"email\":\"updated@test.com\", \"role\":\"USER\"}"));
    }

    @Test
    void testDeleteUser() throws Exception {
        String id = "1";
        when(userService.deleteUser(id)).thenReturn(true);

        mockMvc.perform(delete("/api/users/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}