package dataaccess;
import model.AuthData;

public interface AuthDAO {
    public AuthData addAuth(String username) throws DataAccessException;

    public AuthData getAuthByToken(String token) throws DataAccessException;

    public boolean deleteAuthByToken(String id) throws DataAccessException;

    public void clear() throws DataAccessException;

    public boolean isAuthEmpty() throws DataAccessException;

    public AuthData getAuthByUsername(String username) throws DataAccessException;
}
