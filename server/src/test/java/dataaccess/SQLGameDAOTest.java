package dataaccess;

import chess.*;
import com.google.gson.Gson;
import model.GameData;
import org.junit.jupiter.api.*;
import model.requests.GameTemplateResult;

import java.sql.*;
import java.util.ArrayList;

public class SQLGameDAOTest {

    SQLGameDAO gameDao;

    GameData testGameData;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        gameDao = new SQLGameDAO();
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE games")) {
                statement.executeUpdate();
            }
        }
        ChessGame defaultChessGame = new ChessGame();
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        defaultChessGame.setBoard(board);

        testGameData = new GameData(1, "gamename",null, null, defaultChessGame);
    }

    @Test
    void createGame() throws DataAccessException, SQLException {
        gameDao.addGame(testGameData.gameName());

        GameData resultGameData;

        try (var conn = DatabaseManager.getConnection()) {
            String gameInfo = "gameID, whiteUsername, blackUsername, gameName, chessGame";
            try (var statement = conn.prepareStatement("SELECT "+gameInfo+" FROM games WHERE gameID=?")) {
                statement.setInt(1, testGameData.gameID());
                try (var results = statement.executeQuery()) {
                    results.next();
                    var gameID = results.getInt(("gameID"));
                    var whiteUsername = results.getString("whiteUsername");
                    var blackUsername = results.getString("blackUsername");
                    var gameName = results.getString("gameName");
                    var chessGame = deserializeGame(results.getString("chessGame"));
                    resultGameData = new GameData(gameID, gameName,whiteUsername, blackUsername, chessGame);
                }
            }
        }

        Assertions.assertEquals(testGameData.gameName(), resultGameData.gameName());
    }

    @Test
    void createGameSameName() throws DataAccessException {
        try {
            gameDao.addGame(testGameData.gameName());
            gameDao.addGame(testGameData.gameName());
            ArrayList<GameTemplateResult> resultGames = gameDao.getAllGames();
            if (resultGames.size() == 1) {
                Assertions.assertTrue(true);
            } else {
                Assertions.fail();
            }
        } catch (DataAccessException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void listGames() throws DataAccessException, SQLException {
        gameDao.addGame(testGameData.gameName());
        gameDao.addGame("Game2");

        ArrayList<GameTemplateResult> resultGames = gameDao.getAllGames();

        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, chessGame FROM games")) {
                try (var results = statement.executeQuery()) {
                    int i = 0;
                    while(results.next()) { i++; }
                    Assertions.assertEquals(i, resultGames.size(), "Improper game count in list");
                }
            }
        }
    }

    @Test
    void listGamesEmpty() throws DataAccessException{
        try {
            ArrayList<GameTemplateResult> resultGames = gameDao.getAllGames();
            if (resultGames.size() == 0) {
                Assertions.assertTrue(true);
            } else {
                Assertions.fail();
            }
        } catch (DataAccessException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void getGame() throws DataAccessException {
        try {
            gameDao.addGame(testGameData.gameName());
            GameData checkGame = gameDao.getGameByID(testGameData.gameID());
            if (checkGame.gameName().equals(testGameData.gameName()) && checkGame.gameID() == testGameData.gameID()) {
                Assertions.assertTrue(true);
            } else {
                Assertions.fail();
            }
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    void getGameNegative() {
        try {
            gameDao.addGame(testGameData.gameName());
            GameData checkGame = gameDao.getGameByID(testGameData.gameID());
            Assertions.assertTrue(true);
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    void clear() throws DataAccessException, SQLException {
        gameDao.addGame(testGameData.gameName());
        gameDao.clear();

        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT gameID FROM games WHERE gameID=?")) {
                statement.setInt(1, testGameData.gameID());
                try (var results = statement.executeQuery()) {
                    Assertions.assertFalse(results.next()); //There should be no elements
                }
            }
        }
    }

    private ChessGame deserializeGame(String serializedGame) {
        return new Gson().fromJson(serializedGame, ChessGame.class);
    }
}