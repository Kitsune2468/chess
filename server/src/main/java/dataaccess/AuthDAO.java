package dataaccess;
import model.AuthData;

public interface AuthDAO {
    public AuthData addAuth(String username);
    public AuthData getAuthByToken(String token);
    public boolean deleteAuthByToken(String id);
    public void clear();
    public boolean isEmpty();
}
