package service;

import dataaccess.*;
import handlers.WebSocketHandler;

public class DataBaseService {
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;
    private WebSocketHandler webSocketHandler;

    public DataBaseService(AuthDAO inputAuthDAO, GameDAO inputGameDAO, UserDAO inputUserDAO, WebSocketHandler webSocketHandler) {
        this.authDAO = inputAuthDAO;
        this.gameDAO = inputGameDAO;
        this.userDAO = inputUserDAO;
        this.webSocketHandler = webSocketHandler;
    }

    public void clear() throws DataAccessException {
        authDAO.clear();
        gameDAO.clear();
        userDAO.clear();
        webSocketHandler.clearSessions();
    }

    public boolean checkEmpty() throws DataAccessException {
        if (authDAO.isAuthEmpty() &&
                gameDAO.isGameEmpty() &&
                userDAO.isUsersEmpty()){
            return true;
        } else {
            return false;
        }
    }
}


