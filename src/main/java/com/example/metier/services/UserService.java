package com.example.metier.services;

import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import com.example.metier.entities.User;

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
}
