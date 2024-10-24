package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

public class DataBaseServiceTest {
    private static DataBaseService controlDataBaseService;

    @BeforeAll
    public static void createDataBaseService() throws Exception {
        controlDataBaseService = new DataBaseService(new MemoryAuthDAO(), new MemoryGameDAO(), new MemoryUserDAO());

    }

    @Test
    public void testClear() throws Exception {
        MemoryAuthDAO testMemoryAuth = new MemoryAuthDAO();
        testMemoryAuth.addAuth(new AuthData("1234","username"));

        MemoryGameDAO testMemoryGame = new MemoryGameDAO();
        testMemoryGame.addGame("testGame");

        MemoryUserDAO testMemoryUser = new MemoryUserDAO();
        testMemoryUser.addUser(new UserData("username","password","generic@email.com"));

        DataBaseService testBaseService = new DataBaseService(testMemoryAuth, testMemoryGame, testMemoryUser);
        testBaseService.clear();

        Assertions.assertTrue(testBaseService.checkEmpty());
    }
}
