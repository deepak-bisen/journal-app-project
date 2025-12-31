package com.ghtkdb.journal.application.service.impl;

import com.ghtkdb.journal.application.repository.UserRepository;
import com.ghtkdb.journal.application.service.UserService;
import com.ghtkdb.journal.application.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User createNewUser(User user) {
        log.info("creating new user...");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        log.info("user created.");
        return user;
    }

    @Override
    public User createNewAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ADMIN");
        userRepository.save(user);
        return user;
    }
    public User createUser(User user){
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(String uuid) {
        return userRepository.findById(uuid);
    }

    @Override
    public User updateUserById(User newUser) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User userInDB = userRepository.findByUserName(userName);
        userInDB.setUserName(newUser.getUserName());
        userInDB.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userInDB.setUserName(newUser.getUserName());
        userInDB.setEmail(newUser.getEmail());
        userInDB.setSentimentAnalysis(newUser.isSentimentAnalysis());
        userRepository.save(userInDB);

        return userInDB;
    }

    @Override
    public void deleteUserByUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         userRepository.deleteUserByUserName(authentication.getName());
    }
}
