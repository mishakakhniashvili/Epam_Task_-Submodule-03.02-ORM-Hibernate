package com.epam.gymcrm.facade;

import com.epam.gymcrm.entity.Trainee;
import com.epam.gymcrm.entity.Trainer;
import com.epam.gymcrm.entity.Training;
import com.epam.gymcrm.service.TraineeService;
import com.epam.gymcrm.service.TrainerService;
import com.epam.gymcrm.service.TrainingService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public GymFacade(TraineeService traineeService,
                     TrainerService trainerService,
                     TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public Trainee createTrainee(Trainee trainee) {
        return traineeService.create(trainee);
    }

    public Trainee updateTrainee(String username, String password, Trainee trainee) {
        return traineeService.update(username, password, trainee);
    }

    public void deleteTraineeById(Long id) {
        traineeService.deleteById(id);
    }

    public Optional<Trainee> findTraineeById(Long id) {
        return traineeService.findById(id);
    }

    public List<Trainee> findAllTrainees() {
        return traineeService.findAll();
    }

    public Trainer createTrainer(Trainer trainer) {
        return trainerService.create(trainer);
    }

    public Trainer updateTrainer(String username, String password, Trainer trainer) {
        return trainerService.update(username, password, trainer);
    }

    public Optional<Trainer> findTrainerById(Long id) {
        return trainerService.findById(id);
    }

    public List<Trainer> findAllTrainers() {
        return trainerService.findAll();
    }

    public Training createTraining(Training training) {
        return trainingService.create(training);
    }

    public Optional<Training> findTrainingById(Long id) {
        return trainingService.findById(id);
    }

    public List<Training> findAllTrainings() {
        return trainingService.findAll();
    }

    public Optional<Trainee> findTraineeByUsername(String authUsername,String authPassword,String targetUsername) {
        return traineeService.findByUsername(authUsername, authPassword, targetUsername);
    }

    public Optional<Trainer> findTrainerByUsername(String authUsername,String authPassword,String targetUsername) {
        return trainerService.findByUsername(authUsername, authPassword, targetUsername);
    }

    public boolean isTraineeCredentialsValid(String username, String password) {
        return traineeService.isCredentialsValid(username, password);
    }

    public boolean isTrainerCredentialsValid(String username, String password) {
        return trainerService.isCredentialsValid(username, password);
    }

    public void changeTrainerPassword(String username, String oldPassword, String newPassword) {
        trainerService.changePassword(username, oldPassword, newPassword);
    }

    public void changeTraineePassword(String username, String oldPassword, String newPassword) {
        traineeService.changePassword(username, oldPassword, newPassword);
    }

    public void activateTrainee(String username, String password) {
        traineeService.activate(username, password);
    }

    public void deactivateTrainee(String username, String password) {
        traineeService.deactivate(username, password);
    }

    public void activateTrainer(String username, String password) {
        trainerService.activate(username, password);
    }

    public void deactivateTrainer(String username, String password) {
        trainerService.deactivate(username, password);
    }

}