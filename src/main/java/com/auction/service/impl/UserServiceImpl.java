package com.auction.service.impl;

import com.auction.model.User;
import com.auction.repository.UserRepository;
import com.auction.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User createUser(User user) { return userRepository.save(user); }

    @Override
    public User getUserById(Long id) { return userRepository.findById(id).orElse(null); }

    @Override
    public List<User> getAllUsers() { return userRepository.findAll(); }
}