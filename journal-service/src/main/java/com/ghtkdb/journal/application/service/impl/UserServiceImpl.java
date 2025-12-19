package com.ghtkdb.journal.application.service.impl;

import com.ghtkdb.journal.application.entity.JournalEntry;
import com.ghtkdb.journal.application.repository.UserRepository;
import com.ghtkdb.journal.application.service.UserService;
import com.ghtkdb.journal.application.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        userRepository.save(user);
        return user;
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
    public User updateUserById(String uuid, User newUser) {

        User oldUser = userRepository.findById(uuid).orElseThrow(null);

        if (oldUser != null ){
            oldUser.setUserName(newUser.getUserName());
            oldUser.setPassword(newUser.getPassword());
            userRepository.save(oldUser);
        }

        return oldUser;
    }

    @Override
    public void deleteUserByUserName(String userName) {
         userRepository.deleteUserByUserName(userName);
    }
}
