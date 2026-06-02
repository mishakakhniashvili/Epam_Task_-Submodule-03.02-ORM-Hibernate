package com.epam.gymcrm.service;

import com.epam.gymcrm.dao.TrainerDao;
import com.epam.gymcrm.dao.UserDao;
import com.epam.gymcrm.entity.Trainee;
import com.epam.gymcrm.entity.Trainer;
import com.epam.gymcrm.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerService.class);
    private TrainerDao trainerDao;
    private UsernameGenerator usernameGenerator;
    private PasswordGenerator passwordGenerator;
    private UserDao userDao;

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Autowired
    public void setUsernameGenerator(UsernameGenerator usernameGenerator) {
        this.usernameGenerator = usernameGenerator;
    }

    @Autowired
    public void setPasswordGenerator(PasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }

    @Transactional
    public Trainer create(Trainer trainer) {
        User user = trainer.getUser();
        String username = generateUsername(trainer);

        user.setUsername(username);
        user.setPassword(passwordGenerator.generatePassword());
        user.setActive(true);

        Trainer createdTrainer = trainerDao.save(trainer);
        LOGGER.info("Created trainer with id={} and username={}",
                createdTrainer.getId(),
                createdTrainer.getUser().getUsername());

        return createdTrainer;
    }

    @Transactional
    public Trainer update(String username, String password, Trainer trainer) {
        validateCredentials(username, password);
        LOGGER.info("Updating trainer with id={}", trainer.getId());
        return trainerDao.update(trainer);
    }

    public Optional<Trainer> findById(Long id) {

        LOGGER.info("Finding trainer with id={}", id);
        return trainerDao.findById(id);
    }

    public List<Trainer> findAll() {
        LOGGER.info("finding all trainers");
        return trainerDao.findAll();
    }

    //finds every existing username and generates a new one according to the rules
    private String generateUsername(Trainer trainer) {
        User user = trainer.getUser();
        List<String> existingUsernames = userDao.findAllUsernames();

        return usernameGenerator.generateUsername(
                user.getFirstName(),
                user.getLastName(),
                existingUsernames
        );
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<Trainer> findByUsername(String authUsername, String authPassword, String targetUsername) {
        validateCredentials(authUsername, authPassword);
        LOGGER.info("Finding trainer with username={}", targetUsername);
        return trainerDao.findByUsername(targetUsername);
    }


    public boolean isCredentialsValid(String username, String password) {
        return trainerDao.findByUsername(username)
                .map(trainer -> trainer.getUser().getPassword().equals(password))
                .orElse(false);
    }

    public void validateCredentials(String username, String password){
        if(!isCredentialsValid(username, password)){
            throw new SecurityException("Invalid credentials entered");
        }
    }

    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        validateCredentials(username, oldPassword);

        Trainer trainer = trainerDao.findByUsername(username).orElseThrow();

        trainer.getUser().setPassword(newPassword);

        trainerDao.update(trainer);

        LOGGER.info("Changed password for username={}", username);
    }


    @Transactional
    public void activate(String username, String password){
        validateCredentials(username, password);
        Trainer trainer = trainerDao.findByUsername(username).orElseThrow();
        if(trainer.getUser().isActive()){
            throw new IllegalStateException("Trainer is already active.");}
        trainer.getUser().setActive(true);
        trainerDao.update(trainer);
        LOGGER.info("Activated trainer with id={}", trainer.getId());
    }

    @Transactional
    public void deactivate(String username, String password){
        validateCredentials(username, password);
        Trainer trainer = trainerDao.findByUsername(username).orElseThrow();
        if(!trainer.getUser().isActive()){
            throw new IllegalStateException("Trainer is already inactive.");}
        trainer.getUser().setActive(false);
        trainerDao.update(trainer);
        LOGGER.info("Deactivated trainer with id={}", trainer.getId());
    }

}