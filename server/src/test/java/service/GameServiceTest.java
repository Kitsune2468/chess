package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import org.junit.jupiter.api.*;
import model.requests.GameListResult;
import model.requests.GameTemplateResult;

import java.util.ArrayList;

public class GameServiceTest {
    private static GameService controlGameService;

    @BeforeEach
    public  void createGameService() throws Exception {
        controlGameService = new GameService(new MemoryGameDAO(), new MemoryAuthDAO());
    }

    @Test
    public void testAddGame() throws Exception {
        MemoryGameDAO testMemoryGameDAO = new MemoryGameDAO();
        testMemoryGameDAO.addGame("TestGame");

        controlGameService = new GameService(testMemoryGameDAO, new MemoryAuthDAO());

        try {
            MemoryAuthDAO modifiedAuthDAO = new MemoryAuthDAO();
            modifiedAuthDAO.addAuth(new AuthData("123", "testUsername"));
            GameService testGameService = new GameService(new MemoryGameDAO(), modifiedAuthDAO);
            testGameService.createGame("TestGame","123");
            Assertions.assertEquals(testGameService, controlGameService);
        } catch (Exception e) {
            Assertions.fail();
        }

    }

    @Test
    public void testAddExistingGame() throws Exception {
        MemoryGameDAO testGameDAO = new MemoryGameDAO();
        testGameDAO.addGame("TestGame");
        controlGameService = new GameService(testGameDAO, new MemoryAuthDAO());

        try {
            MemoryAuthDAO modifiedAuthDAO = new MemoryAuthDAO();
            modifiedAuthDAO.addAuth(new AuthData("123", "testUsername"));
            GameService testGameService = new GameService(new MemoryGameDAO(), modifiedAuthDAO);
            testGameService.createGame("TestGame","123");
            testGameService.createGame("TestGame", "123");
            Assertions.fail();
        } catch (DataAccessException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void testJoin() throws Exception {
        MemoryGameDAO testGameDAO = new MemoryGameDAO();
        testGameDAO.addGame("TestGame");
        controlGameService = new GameService(testGameDAO, new MemoryAuthDAO());

        try {
            MemoryAuthDAO modifiedAuthDAO = new MemoryAuthDAO();
            modifiedAuthDAO.addAuth(new AuthData("123", "testUsername"));
            GameService testGameService = new GameService(new MemoryGameDAO(), modifiedAuthDAO);
            testGameService.createGame("TestGame","123");
            testGameService.joinGame(1,"WHITE","123");
            Assertions.assertTrue(true);
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void testSameTeamJoin() throws Exception {
        MemoryGameDAO testGameDAO = new MemoryGameDAO();
        testGameDAO.addGame("TestGame");
        controlGameService = new GameService(testGameDAO, new MemoryAuthDAO());

        try {
            MemoryAuthDAO modifiedAuthDAO = new MemoryAuthDAO();
            modifiedAuthDAO.addAuth(new AuthData("123", "testUsername"));
            GameService testGameService = new GameService(new MemoryGameDAO(), modifiedAuthDAO);
            testGameService.createGame("TestGame","123");
            testGameService.joinGame(1,"WHITE","123");
            testGameService.joinGame(1,"WHITE","123");
            Assertions.fail();
        } catch (DataAccessException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void testListGame() throws Exception {
        GameTemplateResult testResult = new GameTemplateResult(1, "testUsername", null,  "TestGame", new ChessGame(), true);
        GameListResult testList = new GameListResult(new ArrayList<GameTemplateResult>());
        testList.games().add(testResult);

        try {
            MemoryAuthDAO modifiedAuthDAO = new MemoryAuthDAO();
            modifiedAuthDAO.addAuth(new AuthData("123", "testUsername"));
            GameService testGameService = new GameService(new MemoryGameDAO(), modifiedAuthDAO);
            testGameService.createGame("TestGame","123");
            testGameService.joinGame(1,"WHITE","123");
            GameListResult checkList = testGameService.listGames("123");
            Assertions.assertEquals(testList.toString(),checkList.toString());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void testListGameWrongToken() throws Exception {
        GameTemplateResult testResult = new GameTemplateResult(1, "testUsername", null,  "TestGame", new ChessGame(), true);
        GameListResult testList = new GameListResult(new ArrayList<GameTemplateResult>());
        testList.games().add(testResult);

        try {
            MemoryAuthDAO modifiedAuthDAO = new MemoryAuthDAO();
            modifiedAuthDAO.addAuth(new AuthData("123", "testUsername"));
            GameService testGameService = new GameService(new MemoryGameDAO(), modifiedAuthDAO);
            testGameService.createGame("TestGame","123");
            testGameService.joinGame(1,"WHITE","123");
            GameListResult checkList = testGameService.listGames("456");
            Assertions.fail();
        } catch (DataAccessException e) {
            Assertions.assertTrue(true);
        }
    }
}