package com.ghtkdb.journal.application.controller.impl;

import com.ghtkdb.journal.application.controller.UserController;
import com.ghtkdb.journal.application.entity.User;
import com.ghtkdb.journal.application.entity.WeatherResponse;
import com.ghtkdb.journal.application.service.UserService;
import com.ghtkdb.journal.application.service.impl.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class UserControllerImpl implements UserController {

    @Autowired
    private UserService userService;

    @Autowired
    WeatherService weatherService;

    @Override
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("inside @class UserControllerImpl in @method getAllUsers");

        try {
            List<User> allUsers = userService.getAllUsers();
            if (allUsers != null && !allUsers.isEmpty()) {
                log.info("getting All Users {}", userService.getAllUsers());
                return new ResponseEntity<>(allUsers, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("some error occurred when getting users!");
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @Override
    public ResponseEntity<?> greeting() {
        log.info("inside @class UserControllerImpl in @method greeting");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather("Indore");
        String greeting = "";
        if (weatherResponse != null) {
            greeting = ", Weather feels like " + weatherResponse.getCurrent().getFeelslike();
        }
        return new ResponseEntity<>("Hi " + authentication.getName() + greeting, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> getUserById(@PathVariable String uuid) {

        log.info("inside @class UserControllerImpl in @method getUserById");
        Optional<User> user = userService.getUserById(uuid);
        try {
            if (user.isPresent()) {
                log.info("getting user : {}", user.get());
                return new ResponseEntity<>(user.get(), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.info("error while getting user! : ", e);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @Override
    public ResponseEntity<?> deleteUser() {
        try {
            userService.deleteUserByUserName();
            log.info("User Deleted.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Can't delete user!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        try {
            log.info("user updated..");
            return new ResponseEntity<>(userService.updateUserById(user), HttpStatus.OK);
        } catch (Exception e) {
            log.error("entry not found! to delete");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}