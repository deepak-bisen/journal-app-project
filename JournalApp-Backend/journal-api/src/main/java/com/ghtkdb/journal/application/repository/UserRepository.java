package com.ghtkdb.journal.application.repository;

import com.ghtkdb.journal.application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, String> {

    public User findByUserName(String username);

    @Transactional
    public void deleteUserByUserName(String username);
}