package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import model.GameData;
import model.requests.GameTemplateResult;

import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws DataAccessException {
        configureGameDatabase();
    }

    @Override
    public int addGame(String gameName) throws DataAccessException {
        var jsonGame = new Gson().toJson(new ChessGame());
        if (getGameByString(gameName) != null) {
            throw new DataAccessException("bad request");
        }

        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("INSERT INTO games (gameName, chessGame) VALUES(?, ?)", RETURN_GENERATED_KEYS)) {
                statement.setString(1, gameName);
                statement.setString(2, jsonGame);
                statement.executeUpdate();

                var results = statement.getGeneratedKeys();
                if (results.next()) {
                    int gameID = results.getInt(1);
                    return gameID;
                } else {
                    throw new DataAccessException("Failed to add game");
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameData getGameByID(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT gameName, whiteUsername, blackusername, chessGame FROM games WHERE gameID=?")) {
                statement.setInt(1, gameID);
                try (var results = statement.executeQuery()) {
                    results.next();
                    String gameName = results.getString("gameName");
                    String whiteUsername = results.getString("whiteUsername");
                    String blackUsername = results.getString("blackUsername");
                    String jsonGame = results.getString("chessGame");
                    ChessGame chessGame = new Gson().fromJson(jsonGame, ChessGame.class);
                    return new GameData(gameID, gameName, whiteUsername, blackUsername,chessGame);
                }
            }
        } catch (SQLException e) {
            return null;
            //throw new DataAccessException("Game with ID: " + gameID + " could not be found");
        }
    }

    @Override
    public GameData getGameByString(String gameName) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT gameID, whiteUsername, blackusername, chessGame FROM games WHERE gameName=?")) {
                statement.setString(1, gameName);
                try (var results = statement.executeQuery()) {
                    results.next();
                    int gameID = results.getInt("gameID");
                    String whiteUsername = results.getString("whiteUsername");
                    String blackUsername = results.getString("blackUsername");
                    String jsonGame = results.getString("chessGame");
                    ChessGame chessGame = new Gson().fromJson(jsonGame, ChessGame.class);
                    return new GameData(gameID, gameName, whiteUsername, blackUsername,chessGame);
                }
            }
        } catch (SQLException e) {
            return null;
            //throw new DataAccessException("Game: " + gameName + " could not be found");
        }
    }

    @Override
    public void joinGame(int gameID, String joinTeamColor, String playerName) throws DataAccessException {
        GameData foundGame = getGameByID(gameID);
        String blackUsername, whiteUsername;

        switch (joinTeamColor) {
            case "WHITE":
                blackUsername = foundGame.blackUsername();
                whiteUsername = playerName;
                break;
            case "BLACK":
                blackUsername = playerName;
                whiteUsername = foundGame.whiteUsername();
                break;
            case null, default:
                throw new DataAccessException("Failed to join game");
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("UPDATE games SET whiteUsername=?, blackUsername=? WHERE gameID=?")) {
                preparedStatement.setString(1, whiteUsername);
                preparedStatement.setString(2, blackUsername);
                preparedStatement.setInt(3,gameID);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public ArrayList<GameTemplateResult> getAllGames() throws DataAccessException {
        ArrayList<GameTemplateResult> games = new ArrayList<GameTemplateResult>();
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT gameID, gameName, whiteUsername, blackusername, chessGame FROM games")) {
                try (var results = preparedStatement.executeQuery()) {
                    while (results.next()) {
                        int gameID = results.getInt("gameID");
                        String gameName = results.getString("gameName");
                        String whiteUsername = results.getString("whiteUsername");
                        String blackUsername = results.getString("blackUsername");
                        String jsonGame = results.getString("chessGame");
                        ChessGame chessGame = new Gson().fromJson(jsonGame, ChessGame.class);
                        GameTemplateResult convertedGame = new GameTemplateResult(gameID, whiteUsername, blackUsername, gameName,chessGame);
                        games.add(convertedGame);
                    }
                    return games;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE games")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Clear games failed");
        }
    }

    @Override
    public boolean isGameEmpty() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT count(*) AS authCount FROM auth")) {
                try (var results = statement.executeQuery()) {
                    results.next();
                    int gameCount = results.getInt("gameCount");
                    if (gameCount == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to check if empty");
        }
    }

    @Override
    public GameData makeMove(String authToken, GameData gameData, ChessMove givenMove) throws Exception {
        try {
            GameData foundGame = getGameByID(gameData.gameID());
            foundGame.game().makeMove(givenMove);
            var jsonGame = new Gson().toJson(foundGame.game());

            try (var conn = DatabaseManager.getConnection()) {
                try (var preparedStatement = conn.prepareStatement("UPDATE games SET game=? WHERE gameID=?")) {
                    preparedStatement.setString(1, jsonGame);
                    preparedStatement.setInt(2, gameData.gameID());

                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return gameData;
    }

    private final String[] createGameStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `gameName` varchar(256) NOT NULL,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `chessGame` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(gameID),
              INDEX(gameName)
              
            )
            """
    };

    private void configureGameDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createGameStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure gamedatabase: %s", e.getMessage()));
        }
    }
}
