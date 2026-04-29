package com.auction.service.impl;

import com.auction.dto.RegisterRequest;
import com.auction.model.Role;
import com.auction.model.User;
import com.auction.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerShouldCreateUserWithUserRole() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("newuser@test.local");
        request.setPassword("123456");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("newuser@test.local")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.register(request);

        assertEquals("newuser", savedUser.getUsername());
        assertEquals("newuser@test.local", savedUser.getEmail());
        assertEquals("encoded-password", savedUser.getPassword());
        assertEquals(Role.USER, savedUser.getRole());
        assertTrue(savedUser.isEnabled());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals(Role.USER, captor.getValue().getRole());
    }

    @Test
    void registerShouldThrowWhenUsernameExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existing");
        request.setEmail("mail@test.local");
        request.setPassword("123456");

        when(userRepository.existsByUsername("existing")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.register(request));
        assertEquals("Пользователь с таким логином уже существует", exception.getMessage());
    }

    @Test
    void registerShouldThrowWhenEmailExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("user1");
        request.setEmail("existing@test.local");
        request.setPassword("123456");

        when(userRepository.existsByUsername("user1")).thenReturn(false);
        when(userRepository.existsByEmail("existing@test.local")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.register(request));
        assertEquals("Пользователь с таким email уже существует", exception.getMessage());
    }
}