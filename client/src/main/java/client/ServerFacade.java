package client;

import dataaccess.DataAccessException;

import java.net.*;

public class ServerFacade {
    String authToken;
    String serverUrl;

    public ServerFacade(int port) {
        serverUrl = "http://localhost:"+port;
    }

    public void login(String username, String password) throws DataAccessException {

    }

    public void register(String username, String password, String email) throws DataAccessException {
    }
}
