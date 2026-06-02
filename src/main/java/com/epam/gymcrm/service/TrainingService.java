package com.epam.gymcrm.service;

import com.epam.gymcrm.dao.TrainingDao;
import com.epam.gymcrm.entity.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingService.class);
    private TrainingDao trainingDao;

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao){
        this.trainingDao = trainingDao;
    }

    @Transactional
    public Training create(Training training) {
        Training createdTraining = trainingDao.save(training);

        LOGGER.info("Creating training with id={} and name={}", createdTraining.getId(), createdTraining.getTrainingName());

        return createdTraining;
    }

    public Optional<Training> findById(Long id) {
        LOGGER.info("finding training with id={}", id);
        return trainingDao.findById(id);
    }

    public List<Training> findAll() {
        return trainingDao.findAll();
    }
}