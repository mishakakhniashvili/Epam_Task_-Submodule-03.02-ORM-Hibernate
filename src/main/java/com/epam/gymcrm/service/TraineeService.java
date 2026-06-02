package com.epam.gymcrm.service;

import com.epam.gymcrm.dao.TraineeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

import com.epam.gymcrm.entity.Trainee;
import com.epam.gymcrm.entity.User;
import com.epam.gymcrm.dao.UserDao;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TraineeService {

    private UserDao userDao;
    private TraineeDao traineeDao;
    private UsernameGenerator usernameGenerator;
    private PasswordGenerator passwordGenerator;
    private static final Logger LOGGER = LoggerFactory.getLogger(TraineeService.class);
    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }


    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
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
    public Trainee create(Trainee trainee) {
        User user = trainee.getUser();
        String username = generateUsername(trainee);

        user.setUsername(username);
        user.setPassword(passwordGenerator.generatePassword());
        user.setActive(true);

        Trainee createdTrainee = traineeDao.save(trainee);

        LOGGER.info("Created trainee with id={} and username={}",
                createdTrainee.getId(),
                createdTrainee.getUser().getUsername());

        return createdTrainee;
    }

    //finds every existing username and generates a new one according to the rules
    private String generateUsername(Trainee trainee) {
        User user = trainee.getUser();
        List<String> existingUsernames = userDao.findAllUsernames();

        return usernameGenerator.generateUsername(
                user.getFirstName(),
                user.getLastName(),
                existingUsernames
        );
    }

    @Transactional
    public Trainee update(String username, String password, Trainee trainee) {
        validateCredentials(username, password);
        LOGGER.info("Updating trainee with id={}", trainee.getId());
        return traineeDao.update(trainee);
    }

    @Transactional
    public void deleteById(Long id) {
        LOGGER.info("Deleting trainee with id={}", id);
        traineeDao.deleteById(id);
    }

    public Optional<Trainee> findById(Long id) {
        LOGGER.info("finding trainee with id={}", id);
        return traineeDao.findById(id);
    }

    public List<Trainee> findAll() {
        return traineeDao.findAll();
    }

    public Optional<Trainee> findByUsername(String authUsername, String authPassword, String targetUsername) {
        validateCredentials(authUsername, authPassword);
        LOGGER.info("Finding trainee with username={}", targetUsername);
        return traineeDao.findByUsername(targetUsername);
    }

    public boolean isCredentialsValid(String username, String password) {
        return traineeDao.findByUsername(username)
                .map(trainee -> trainee.getUser().getPassword().equals(password))
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

        Trainee trainee = traineeDao.findByUsername(username).orElseThrow();

        trainee.getUser().setPassword(newPassword);

        traineeDao.update(trainee);

        LOGGER.info("Changed password for username={}", username);
    }

    @Transactional
    public void activate(String username, String password){
        validateCredentials(username, password);
        Trainee trainee = traineeDao.findByUsername(username).orElseThrow();
        if(trainee.getUser().isActive()){throw new IllegalStateException("Trainee is already active.");}
        trainee.getUser().setActive(true);
        traineeDao.update(trainee);
        LOGGER.info("Activated trainee with id={}", trainee.getId());
    }

    @Transactional
    public void deactivate(String username, String password){
        validateCredentials(username, password);
        Trainee trainee = traineeDao.findByUsername(username).orElseThrow();
        if(!trainee.getUser().isActive()){throw new IllegalStateException("Trainee is already inactive.");}
        trainee.getUser().setActive(false);
        traineeDao.update(trainee);
        LOGGER.info("Deactivated trainee with id={}", trainee.getId());
    }

}