package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class SQLUserDAO implements UserDAO {
    private ArrayList<UserData> memoryUsers;

    public SQLUserDAO() {
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
    public boolean isEmpty() {
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
        SQLUserDAO that = (SQLUserDAO) o;
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
