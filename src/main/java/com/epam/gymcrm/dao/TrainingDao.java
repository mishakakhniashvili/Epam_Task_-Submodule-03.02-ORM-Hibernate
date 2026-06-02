package com.epam.gymcrm.dao;

import com.epam.gymcrm.entity.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainingDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Training save(Training training) {
        entityManager.persist(training);
        return training;
    }

    public Optional<Training> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Training.class, id));
    }

    public List<Training> findAll() {
        return entityManager.createQuery("select tr from Training tr", Training.class).getResultList();
    }
}