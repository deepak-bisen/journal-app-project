package com.ghtkdb.journal.application.controller;

import com.ghtkdb.journal.application.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public interface UserController {

    @GetMapping("/get")
    ResponseEntity<List<User>> getAllUsers();

    @GetMapping("/get/{userId}")
    ResponseEntity<User> getUserById(@PathVariable String userId);

    @PostMapping("/create")
    ResponseEntity<User> createUser(@RequestBody User user);

    @DeleteMapping("delete/{userName}")
    ResponseEntity<?> deleteUserByUserName(@PathVariable String userName);

    @PutMapping("update/{userId}")
    ResponseEntity<User> updateUserById(@PathVariable String userId, @RequestBody User user);
}
