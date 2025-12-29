package com.ghtkdb.journal.application.controller.impl;

import com.ghtkdb.journal.application.controller.AdminController;
import com.ghtkdb.journal.application.entity.User;
import com.ghtkdb.journal.application.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class AdminControllerImpl implements AdminController {
    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            log.info("getting all users...");
            List<User> allUser = userService.getAllUsers();
            if (!allUser.isEmpty()) {
                return new ResponseEntity<>(allUser, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("error occurred while getting users: {}", e);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<User> createNewAdmin(User user){
        try{
            log.info("creating admin..");
            return new ResponseEntity<>(userService.createNewAdmin(user),HttpStatus.CREATED);
        }catch (Exception e){
            log.error("can't create admin! {}", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
