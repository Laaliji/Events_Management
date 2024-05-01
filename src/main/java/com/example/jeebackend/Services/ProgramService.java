package com.example.jeebackend.Services;

import com.example.jeebackend.Entities.Program;
import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

@Stateless
@Local
public class ProgramService {

    private EntityManagerFactory emf;
    private EntityManager em;

    public ProgramService() {
        emf = Persistence.createEntityManagerFactory("jee");
        em = emf.createEntityManager();
    }

    public void createProgram(Program program) {
        try {
            em.getTransaction().begin();
            em.persist(program);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Program findProgramById(Long id) {
        try {
            return em.find(Program.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Program> findAllPrograms() {
        try {
            return em.createQuery("SELECT p FROM Program p", Program.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateProgram(long id, Program updatedProgram) {
        try {
            em.getTransaction().begin();
            Program existingProgram = em.find(Program.class, id);
            if (existingProgram != null) {
                existingProgram.setSchedules(updatedProgram.getSchedules());
                existingProgram.setSpeakers(updatedProgram.getSpeakers());
                existingProgram.setTopics(updatedProgram.getTopics());
                existingProgram.setImages(updatedProgram.getImages());
                existingProgram.setDetails(updatedProgram.getDetails());
                existingProgram.setEvent(updatedProgram.getEvent());
                em.merge(existingProgram);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteProgramByID(long programId) {
        try {
            em.getTransaction().begin();
            Program programToRemove = em.find(Program.class, programId);
            if (programToRemove != null) {
                em.remove(programToRemove);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
