package com.ghtkdb.journal.application.service;

import com.ghtkdb.journal.application.entity.JournalEntry;
import com.ghtkdb.journal.application.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    public User createUser(User user);

    public User createNewUser(User user);

    public User createNewAdmin(User user);

    public List<User> getAllUsers();

    public Optional<User> getUserById(String uuid);

    public User updateUserById(User user);

    public void deleteUserByUserName();
}
