package com.ghtkdb.journal.application.controller;

import com.ghtkdb.journal.application.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/public")
public interface PublicController {

    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody User user);
}
