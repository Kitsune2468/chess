package dataaccess;
import model.AuthData;

public interface AuthDAO {
    public void addAuth(AuthData authData);
    public AuthData getAuthByID(String id);
    public void deleteAuthByID(String id);
    public void clear();
    public boolean isEmpty();
}
