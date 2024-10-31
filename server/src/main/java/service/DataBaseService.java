package service;

import dataaccess.*;

public class DataBaseService {
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;

    public DataBaseService(AuthDAO inputAuthDAO, GameDAO inputGameDAO, UserDAO inputUserDAO) {
        this.authDAO = inputAuthDAO;
        this.gameDAO = inputGameDAO;
        this.userDAO = inputUserDAO;
    }

    public void clear() throws DataAccessException {
        authDAO.clear();
        gameDAO.clear();
        userDAO.clear();
        return;
    }

    public boolean checkEmpty() throws DataAccessException {
        if (authDAO.isEmpty() &&
                gameDAO.isEmpty() &&
                userDAO.isEmpty()){
            return true;
        } else {
            return false;
        }
    }
}


