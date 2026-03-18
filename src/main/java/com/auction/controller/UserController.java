package com.auction.controller;

import com.auction.model.User;
import com.auction.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping public User createUser(@RequestBody User user){return userService.createUser(user);}
    @GetMapping("/{id}") public User getUser(@PathVariable Long id){return userService.getUserById(id);}
    @GetMapping public List<User> getAllUsers(){return userService.getAllUsers();}
}