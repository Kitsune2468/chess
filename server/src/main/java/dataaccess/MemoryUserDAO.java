package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {
    private ArrayList<UserData> memoryUsers;

    public MemoryUserDAO() {
        memoryUsers = new ArrayList<UserData>();
    }

    @Override
    public void addUser(UserData userData) {
        memoryUsers.add(userData);
    }

    @Override
    public UserData getUserByName(String name) {
        return null;
    }

    @Override
    public void updateUser() {

    }

    @Override
    public void deleteUserByName(String name) {
        memoryUsers.forEach(user -> {
            if (user.username().equals(name)) {
                memoryUsers.remove(user);
            }
        });
    }


    @Override
    public void clear() {
        memoryUsers.clear();
    }
}
