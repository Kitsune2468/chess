package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public class MemoryAuthDAO implements AuthDAO {
    private ArrayList<AuthData> memoryAuths;

    public MemoryAuthDAO() {
        memoryAuths = new ArrayList<AuthData>();
    }

    @Override
    public void addAuth(AuthData authData) {
        memoryAuths.add(authData);
    }

    @Override
    public AuthData getAuthByID(String id) {
        return null;
    }


    @Override
    public void deleteAuthByID(String id) {

    }

    @Override
    public void clear() {
        memoryAuths.clear();
    }
}
