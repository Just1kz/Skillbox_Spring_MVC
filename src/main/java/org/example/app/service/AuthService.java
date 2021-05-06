package org.example.app.service;

import org.apache.log4j.Logger;
import org.example.web.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthRepositoryImpl repository;
    private final Logger logger = Logger.getLogger(AuthService.class);

    @Autowired
    public AuthService(AuthRepositoryImpl repository) {
        this.repository = repository;
    }

    public void register(User user) {
        repository.addUsers(user);
        logger.info(user + " was registered");
    }

    public boolean authenticate(User user) {
        logger.info("try auth with user-form: " + user);
        return repository.findUserByEmailAndPassword(user);
    }
}
