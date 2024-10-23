package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class DataBaseService {
    private static DataBaseService instance;
    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    private static UserDAO userDAO;


    public static void clear() {
        authDAO.clearAuths();
        gameDAO.clearGames();
        userDAO.clearUsers();
        return;
    }

    public static DataBaseService getInstance() {
        if (instance == null) {
            instance = new DataBaseService();
        }
        return instance;
    }
}


