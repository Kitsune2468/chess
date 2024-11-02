package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;


public class UserService {
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public UserService(UserDAO inputUserDAO, AuthDAO inputAuthDAO) {
        userDAO = inputUserDAO;
        authDAO = inputAuthDAO;
    }

    public AuthData addUser(UserData newUserData) throws DataAccessException {
        String foundUsername = newUserData.username();
        String foundPassword = newUserData.password();

        if (foundUsername == null || foundPassword == null) {
            throw new DataAccessException("null username or password");
        }
        UserData foundUser = userDAO.getUserByUsername(newUserData.username());
        if (foundUser != null) {
            throw new DataAccessException("already taken");
        }
        try {
            userDAO.addUser(newUserData);
            AuthData newAuth = authDAO.addAuth(newUserData.username());

            return newAuth;
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public AuthData login(UserData userLoginData) throws DataAccessException {
        try {
            String username = userLoginData.username();
            String password = userLoginData.password();
            AuthData newAuth = null;

            if (username == null || password == null) {
                throw new DataAccessException("null username or password");
            }

            UserData foundUser = userDAO.getUserByUsername(username);
            if (foundUser == null) {
                throw new DataAccessException("Unauthorized");
            }
            String foundUsername = foundUser.username();
            String foundPassword = foundUser.password();

            boolean passwordsMatch;
            if (password.equals(foundPassword)) {
                passwordsMatch = true;
            } else {
                passwordsMatch=BCrypt.checkpw(password, foundPassword);
            }

            if (username.equals(foundUsername) && (passwordsMatch || password.equals(foundPassword))) {
                newAuth = authDAO.addAuth(foundUser.username());
            } else {
                throw new DataAccessException("Unauthorized");
            }
            return newAuth;
        } catch (DataAccessException e) {
            throw e;
        }
    }

    public void logout(String authToken) throws DataAccessException {
        try {
            if (authDAO.deleteAuthByToken(authToken)) {
                return;
            } else {
                throw new DataAccessException("Unauthorized");
            }
        } catch (DataAccessException e) {
            throw e;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        UserService that = (UserService) o;
        return Objects.equals(userDAO, that.userDAO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userDAO, authDAO);
    }

    @Override
    public String toString() {
        return "UserService{" +
                "userDAO=" + userDAO +
                ", authDAO=" + authDAO +
                '}';
    }
}
