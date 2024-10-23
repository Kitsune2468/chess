package dataaccess;
import model.UserData;

public interface UserDAO {
    public void addUser(UserData userData);
    public UserData getUserByName(String name);
    public void updateUser();
    public void deleteUserByName(String name);
    public void clear();
}

