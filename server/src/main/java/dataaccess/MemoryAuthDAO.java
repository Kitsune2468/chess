package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private ArrayList<AuthData> memoryAuths;

    public MemoryAuthDAO() {
        memoryAuths = new ArrayList<AuthData>();
    }

    @Override
    public AuthData addAuth(String username) {
        String token = generateToken();
        AuthData newAuth = new AuthData(token, username);
        memoryAuths.add(newAuth);
        return newAuth;
    }

    public void addAuth(AuthData newAuthData) {
        memoryAuths.add(newAuthData);
    }

    @Override
    public AuthData getAuthByToken(String token) {
        AuthData foundAuth = null;
        for(AuthData searchAuth : memoryAuths) {
            String searchToken = searchAuth.authToken();
            if (searchToken.equals(token)) {
                foundAuth = searchAuth;
            }
        }
        return foundAuth;
    }

    @Override
    public AuthData getAuthByUsername(String username) {
        AuthData foundAuth = null;
        for(AuthData searchAuth : memoryAuths) {
            String searchUsername = searchAuth.username();
            if (searchUsername.equals(username)) {
                foundAuth = searchAuth;
            }
        }
        return foundAuth;
    }

    @Override
    public boolean deleteAuthByToken(String token) {
        for(AuthData searchAuth : memoryAuths) {
            String searchToken = searchAuth.authToken();
            if (searchToken.equals(token)) {
                memoryAuths.remove(searchAuth);
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        memoryAuths.clear();
    }

    @Override
    public boolean isAuthEmpty() {
        if (memoryAuths.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }



    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
