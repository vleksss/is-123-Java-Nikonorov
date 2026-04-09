package com.auction.service;

import com.auction.dto.RegisterRequest;
import com.auction.model.User;

import java.util.List;

public interface UserService {
    User register(RegisterRequest request);
    User getById(Long id);
    User getByUsername(String username);
    List<User> getAll();
}
