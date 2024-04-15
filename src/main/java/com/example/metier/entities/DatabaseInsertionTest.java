package com.example.metier.entities;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class DatabaseInsertionTest {

    public static void main(String[] args) {
        // Assuming you have an EntityManagerFactory configured
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jee");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // Begin a transaction
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            // Create an instance of your entity class
            User user = new User();
            user.setEmail("test@example.com");
            user.setNom("Test");
            user.setPrenom("User");
            user.setPassword("password");
            user.setRole(Role.USER);

            // Persist the entity
            entityManager.persist(user);

            // Commit the transaction
            transaction.commit();

            System.out.println("User inserted successfully with ID: " + user.getId());
        } catch (Exception e) {
            // Rollback the transaction if an error occurs
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            // Close the EntityManager and EntityManagerFactory when done
            entityManager.close();
            entityManagerFactory.close();
        }
    }
}

