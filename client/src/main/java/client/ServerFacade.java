package client;

import java.net.*;

public class ServerFacade {
    String authToken;
    String serverUrl;

    public ServerFacade(int port) throws Exception {
        serverUrl = "http://localhost:"+port;
    }


}
