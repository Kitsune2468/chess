package dataaccess;
import model.UserData;

public interface UserDAO {
    public void addUser(UserData userData);
    public UserData getUserByUsername(String name);
    public void updateUser();
    public void deleteUserByUsername(String name);
    public void clear();
    public boolean isEmpty();
}

