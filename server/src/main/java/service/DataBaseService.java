package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.UserDAO;

public class DataBaseService {
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;

    public DataBaseService(AuthDAO inputAuthDAO, GameDAO inputGameDAO, UserDAO inputUserDAO) {
        this.authDAO = inputAuthDAO;
        this.gameDAO = inputGameDAO;
        this.userDAO = inputUserDAO;
    }

    public void clear() {
        authDAO.clear();
        gameDAO.clear();
        userDAO.clear();
        return;
    }

}


