package client;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.Map;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        try {
            facade = new ServerFacade(port);
        } catch (Exception e) {
            System.out.println("Failed to connect to server: "+e.getMessage());
        }
    }

    @BeforeEach
    void clearServer() {
        try {
            facade.clearServer();
        } catch (Exception e) {
            System.out.println("Failed to connect to server: "+e.getMessage());
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerTest() {
        try {
            facade.register("username", "password", "email");
            Assertions.assertTrue(true);
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void registerTestFail() {
        try {
            facade.register("username", "password", "email");
            facade.register("username", "password", "email");
            Assertions.fail();
        } catch (DataAccessException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void loginTest() {
        try {
            facade.register("username", "password", "email");
            facade.logout();
            facade.login("username","password");
            Assertions.assertTrue(true);
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void loginTestFail() {
        try {
            facade.register("username", "password", "email");
            facade.logout();
            facade.login("username","wrongPassword");
            Assertions.fail();
        } catch (DataAccessException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void logoutTest() {
        try {
            facade.register("username", "password", "email");
            facade.logout();
            Assertions.assertTrue(true);
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void logoutTestFail() {
        try {
            facade.logout();
            Assertions.fail();
        } catch (DataAccessException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void createGameTest() {
        try {
            facade.register("username", "password", "email");
            facade.createGame("testGame");
            Assertions.assertTrue(true);
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void createGameTestFail() {
        try {
            facade.register("username", "password", "email");
            facade.createGame("testGame");
            facade.createGame("testGame");
            Assertions.fail();
        } catch (DataAccessException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void listGamesTest() {
        try {
            facade.register("username", "password", "email");
            facade.createGame("testGame");
            facade.listGames();
            Assertions.assertTrue(true);
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void listGamesTestFail() {
        try {
            facade.register("username", "password", "email");
            facade.listGames();
            Assertions.fail();
        } catch (DataAccessException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void joinGameTest() {
        try {
            facade.register("username", "password", "email");
            facade.createGame("testGame");
            facade.joinGame(1,"WHITE");
            Assertions.assertTrue(true);
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void joinGameTestFail() {
        try {
            facade.register("username", "password", "email");
            facade.joinGame(1,"WHITE");
            Assertions.fail();
        } catch (DataAccessException e) {
            Assertions.assertTrue(true);
        }
    }

}
