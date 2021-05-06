package org.example.app.service;

import org.apache.log4j.Logger;
import org.example.web.dto.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class AuthRepositoryImpl implements AuthRepository, RegisterRepository {
    private final Logger logger = Logger.getLogger(AuthRepositoryImpl.class);
    private final Map<String, User> repo = new HashMap<>();

    @Override
    public void addUsers(User user) {
        repo.putIfAbsent(user.getEmail(), user);
        logger.info("memStore add user: " + user);
    }

    @Override
    public boolean findUserByEmailAndPassword(User user) {
        User rsl = repo.get(user.getEmail());
        return rsl != null;
    }
}
