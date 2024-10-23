package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;

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
        AuthData foundAuth = null;
        for(AuthData searchAuth : memoryAuths) {
            if (searchAuth.authToken() == id) {
                foundAuth = searchAuth;
            }
        }
        return foundAuth;
    }


    @Override
    public void deleteAuthByID(String id) {

    }

    @Override
    public void clear() {
        memoryAuths.clear();
    }

    @Override
    public boolean isEmpty() {
        if (memoryAuths.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
