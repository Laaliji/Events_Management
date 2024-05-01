package com.example.jeebackend.Services;

import com.example.jeebackend.Entities.User;
import com.example.jeebackend.Entities.Remark;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.ejb.EJB;
import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Stateless
@Local
public class UserService {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private AuthenticationService authenticationService;

    public void register(User user) {
        try {
            em.persist(user);
        } catch (Exception e) {
            // Handle the exception appropriately
            e.printStackTrace();
        }
    }

    public String authenticateUser(String email, String password) {
        User user = login(email, password);
        if (user != null) {
            return authenticationService.generateJwtToken(user);
        }
        return null;
    }

    public String loginAndGetJwtToken(String email, String password) {
        User user = login(email, password);
        if (user != null) {
            return authenticationService.generateJwtToken(user);
        } else {
            return null; // Failed to log in
        }
    }

    public User login(String email, String password) {
        User user = findByEmail(email);
        if (user != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }


    public User findByEmail(String email) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email = :email ", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
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
        EntityTransaction tx = em.getTransaction();
        if (existingUser != null) {
            tx.begin();
            existingUser.setNom(updatedUser.getNom());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setRole(updatedUser.getRole());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setPrenom(updatedUser.getPrenom());
            em.merge(existingUser);
            tx.commit();
        }
    }

    public void deleteUserByID(long userId) {

        User userToRemove = findById(userId);
        if (userToRemove != null) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                em.remove(userToRemove);
                tx.commit();
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                throw e;
            }

        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            userToRemove = findById(userId);
            if (userToRemove != null) {
                em.remove(userToRemove);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();

        }
    }

    }

}
