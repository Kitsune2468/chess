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

}
