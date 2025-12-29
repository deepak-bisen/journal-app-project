package com.ghtkdb.journal.application.controller;

import com.ghtkdb.journal.application.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("user")
public interface UserController {

    @GetMapping("/get")
    ResponseEntity<List<User>> getAllUsers();

    @GetMapping("/get/{userId}")
    ResponseEntity<User> getUserById(@PathVariable String uuid);

    @DeleteMapping("/delete")
    ResponseEntity<?> deleteUser();

    @PutMapping("/update")
    ResponseEntity<User> updateUser(@RequestBody User user);
}
