package org.example.app.service;

import org.example.web.dto.User;

public interface AuthRepository {

    boolean findUserByEmailAndPassword(User user);

}
