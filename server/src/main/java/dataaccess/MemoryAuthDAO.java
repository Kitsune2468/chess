package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public class MemoryAuthDAO implements AuthDAO {
    private ArrayList<AuthData> memoryAuths;

    @Override
    public void addAuth(AuthData authData) {
        memoryAuths.add(authData);
    }

    @Override
    public AuthData getAuthByID(String id) {
        return null;
    }

    @Override
    public void updateAuth() {

    }

    @Override
    public void deleteAuthByID(String id) {

    }

    @Override
    public void clearAuths() {
        memoryAuths.clear();
    }
}
