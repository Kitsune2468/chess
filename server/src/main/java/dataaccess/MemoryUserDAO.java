package dataaccess;

import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.ArrayList;
import java.util.Objects;

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
            String searchName =searchUser.username().toString();
            if (searchName.equals(name)) {
                return searchUser;
            }
        }
        return foundUser;
    }

    @Override
    public void clear() {
        memoryUsers.clear();
    }

    @Override
    public boolean isUsersEmpty() {
        if (memoryUsers.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        MemoryUserDAO that = (MemoryUserDAO) o;
        return Objects.equals(memoryUsers, that.memoryUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(memoryUsers);
    }

    @Override
    public String toString() {
        return "MemoryUserDAO{" +
                "memoryUsers=" + memoryUsers +
                '}';
    }
}
