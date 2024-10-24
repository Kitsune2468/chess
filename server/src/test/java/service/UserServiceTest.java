package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

public class UserServiceTest {
    private static UserService controlUserService;

    @BeforeEach
    public  void createUserService() throws Exception {
        controlUserService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
    }

    @Test
    public void testAddUser() throws Exception {
        MemoryUserDAO testMemoryUser = new MemoryUserDAO();
        UserData testUserData = new UserData("username","password","generic@email.com");
        testMemoryUser.addUser(testUserData);
        controlUserService = new UserService(testMemoryUser, new MemoryAuthDAO());

        UserService testUserService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
        testUserService.addUser(testUserData);

        Assertions.assertEquals(controlUserService,testUserService);
    }

    @Test
    public void testAddExistingUsername() throws Exception {
        MemoryUserDAO testMemoryUser = new MemoryUserDAO();
        UserData testUserData = new UserData("bob","pass1","generic@email.com");

        UserService testUserService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
        testUserService.addUser(testUserData);

        UserData existingUserData = new UserData("bob","differentPass","alsoGeneric@email.com");

        try {
            testUserService.addUser(existingUserData);
            Assertions.fail();
        } catch (DataAccessException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void testLogin() throws Exception {
        UserData testUserData = new UserData("username","password","generic@email.com");

        UserService testUserService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
        AuthData testToken = testUserService.addUser(testUserData);
        testUserService.logout(testToken.authToken());

        try {
            testUserService.login(testUserData);
            Assertions.assertTrue(true);
        } catch(DataAccessException e) {
            Assertions.fail("Did not log in");
        }
    }

    @Test
    public void testDoubleLogin() throws Exception {
        UserData testUserData = new UserData("username","password","generic@email.com");

        UserService testUserService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
        AuthData testToken = testUserService.addUser(testUserData);
        testUserService.logout(testToken.authToken());

        try {
            testUserService.login(testUserData);
            testUserService.login(testUserData);
            Assertions.fail("Logged in twice");
        } catch(DataAccessException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void testLogout() throws Exception {
        UserData testUserData = new UserData("username","password","generic@email.com");

        UserService testUserService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
        AuthData testToken = testUserService.addUser(testUserData);

        try {
            testUserService.logout(testToken.authToken());
            Assertions.assertTrue(true);
        } catch(DataAccessException e) {
            Assertions.fail("Did not log in");
        }
    }

    @Test
    public void testLogoutWrongToken() throws Exception {
        UserData testUserData = new UserData("username","password","generic@email.com");

        UserService testUserService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
        testUserService.addUser(testUserData);
        AuthData testToken = new AuthData("thisIsAWrongToken","username");

        try {
            testUserService.logout(testToken.authToken());
            Assertions.fail("Logged out with wrong token");
        } catch(DataAccessException e) {
            Assertions.assertTrue(true);
        }
    }
}