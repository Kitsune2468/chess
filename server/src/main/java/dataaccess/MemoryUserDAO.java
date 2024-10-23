package dataaccess;

import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

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
    public UserData getUserByUsername(String name) {
        UserData foundUser = null;
        for(UserData searchUser : memoryUsers) {
            if (searchUser.username() == name) {
                foundUser = searchUser;
            }
        }
        return foundUser;
    }

    @Override
    public void updateUser() {

    }

    @Override
    public void deleteUserByUsername(String name) {
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

    @Override
    public boolean isEmpty() {
        if (memoryUsers.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
