package com.ghtkdb.journal.application.controller.impl;

import com.ghtkdb.journal.application.controller.PublicController;
import com.ghtkdb.journal.application.entity.User;
import com.ghtkdb.journal.application.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class PublicControllerImpl implements PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "OK";
    }

    @Override
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            log.info("inside @class PublicControllerImpl in @method createUser saving user...");
//            userService.createUser(user);
            userService.createNewUser(user);
            log.info("User Saved : {}", user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Can't create user : {}", user);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}