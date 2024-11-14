package dataaccess;
import model.UserData;

public interface UserDAO {
    public void addUser(UserData userData) throws DataAccessException;

    public UserData getUserByUsername(String name) throws DataAccessException;

    public void clear() throws DataAccessException;

    public boolean isUsersEmpty() throws DataAccessException;
}

