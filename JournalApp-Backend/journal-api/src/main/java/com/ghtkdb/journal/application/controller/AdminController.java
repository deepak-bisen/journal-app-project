package com.ghtkdb.journal.application.controller;

import com.ghtkdb.journal.application.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/admin")
public interface AdminController {
    @GetMapping("/all-users")
    ResponseEntity<List<User>> getAllUsers();

    @PostMapping("/create-admin-user")
    ResponseEntity<User> createNewAdmin(@RequestBody User user);

    @GetMapping("/clear-app-cache")
    public void clearAppCache();
}
