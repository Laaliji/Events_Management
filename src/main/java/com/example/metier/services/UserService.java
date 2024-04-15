package com.example.metier.services;

import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import java.util.List;
import jakarta.persistence.*;
import com.example.metier.entities.User;
import java.util.ArrayList;

@Stateless
@Local
public class UserService {
    EntityManagerFactory emf;
    EntityManager em;

    public UserService() {
        emf = Persistence.createEntityManagerFactory("jee");
        em = emf.createEntityManager();
    }

    public void register(User user) {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(user);
            et.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User login(String email, String password) {
        try{
            User user = em.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :password", User.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getSingleResult();
            if (user != null) {
                return user;
            }
        } catch( NoResultException e)
        {
            return null;
        }
        return null;
    }

    public User findByEmail(String email) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email = :email ", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        }catch (NoResultException e) {
            return null;
        }
    }
    public List<User> findAllUsers() {
        try {
            return em.createQuery("SELECT u FROM User u", User.class)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>(); // Return an empty list if no users are found
        }
    }
    public User findById(long id) {
        return em.find(User.class, id);
    }

    public void updateUser(long userId, User updatedUser) {
        User existingUser = findById(userId);
        if (existingUser != null) {
            existingUser.setNom(updatedUser.getNom());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setRole(updatedUser.getRole());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setPrenom(updatedUser.getPrenom());
            em.merge(existingUser);
        }
    }

    public void deleteUserByID(long userId) {
        User userToRemove = findById(userId);
        if (userToRemove != null) {
            em.remove(userToRemove);
        }
    }


}
