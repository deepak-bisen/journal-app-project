package com.ghtkdb.journal.application.repository;

import com.ghtkdb.journal.application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    public User findByUserName(String username);
    public void deleteUserByUserName(String username);
}