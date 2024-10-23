package service;

import dataaccess.GameDAO;
import dataaccess.UserDAO;

import java.util.UUID;

public class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO inputUserDAO) {
        this.userDAO = inputUserDAO;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
