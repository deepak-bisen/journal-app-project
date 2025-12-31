package com.ghtkdb.journal.application.repository;

import com.ghtkdb.journal.application.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> getUserForSA() {
        // JPQL uses class names (User) and field names (sentimentAnalysis)
        String jpql = "SELECT u FROM User u WHERE u.email LIKE :emailPattern AND u.sentimentAnalysis = true";

        return entityManager.createQuery(jpql, User.class)
                .setParameter("emailPattern", "%@%.%") // Basic check: contains @ and a dot
                .getResultList();
    }
}
