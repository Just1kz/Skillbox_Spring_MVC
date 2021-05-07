package org.example.app.repository;

import org.example.web.dto.User;

public interface AuthRepository {

    boolean findUserByEmailAndPassword(User user);

}
