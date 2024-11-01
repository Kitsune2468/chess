package dataaccess;

import chess.ChessGame;
import model.GameData;
import service.requests.GameTemplateResult;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLGameDAO implements GameDAO {
    private ArrayList<GameData> memoryGames;
    private int gameCounter;

    public SQLGameDAO() throws DataAccessException {
        memoryGames = new ArrayList<GameData>();
        gameCounter = 1;
        configureDatabase();
    }

    @Override
    public int addGame(String gameName) throws DataAccessException {
        GameData newGame = new GameData(gameCounter,null, null, gameName, new ChessGame());
        memoryGames.add(newGame);
        return gameCounter++;
    }

    @Override
    public GameData getGameByID(int gameID) throws DataAccessException {
        GameData foundGame = null;
        for(GameData searchAuth : memoryGames) {
            int foundGameID = searchAuth.gameID();
            if (foundGameID == gameID) {
                foundGame = searchAuth;
            }
        }
        return foundGame;
    }

    @Override
    public GameData getGameByString(String gameName) throws DataAccessException {
        GameData foundGame = null;
        for(GameData searchAuth : memoryGames) {
            String foundGameName = searchAuth.gameName();
            if (foundGameName.equals(gameName)) {
                foundGame = searchAuth;
            }
        }
        return foundGame;
    }

    @Override
    public void joinGame(int gameID, String joinTeamColor, String playerName) throws DataAccessException {
        for(GameData searchGame : memoryGames) {
            int foundGameID = searchGame.gameID();
            if (foundGameID == gameID) {
                if (joinTeamColor.equals("BLACK")) {
                    GameData updatedGame = new GameData(searchGame.gameID(),
                            searchGame.whiteUsername(),
                            playerName,
                            searchGame.gameName(),
                            searchGame.game());
                    memoryGames.remove(searchGame);
                    memoryGames.add(updatedGame);
                } else if (joinTeamColor.equals("WHITE")) {
                    GameData updatedGame = new GameData(searchGame.gameID(),
                            playerName,
                            searchGame.blackUsername(),
                            searchGame.gameName(),
                            searchGame.game());
                    memoryGames.remove(searchGame);
                    memoryGames.add(updatedGame);
                }
            }
        }
    }

    @Override
    public ArrayList<GameTemplateResult> getAllGames() throws DataAccessException {
        ArrayList<GameTemplateResult> games = new ArrayList<GameTemplateResult>();
        for (GameData game : memoryGames) {
            GameTemplateResult convertedGame = new GameTemplateResult(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName());
            games.add(convertedGame);
        }
        return games;
    }

    @Override
    public void clear() throws DataAccessException {
        memoryGames.clear();
    }

    @Override
    public boolean isEmpty() throws DataAccessException {
        if (memoryGames.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    //if (param instanceof String p) ps.setString(i + 1, p);
                    //else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    //else if (param instanceof PetType p) ps.setString(i + 1, p.toString());
                    //else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  pet (
              `id` int NOT NULL AUTO_INCREMENT,
              `name` varchar(256) NOT NULL,
              `type` ENUM('CAT', 'DOG', 'FISH', 'FROG', 'ROCK') DEFAULT 'CAT',
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(type),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        SQLGameDAO that = (SQLGameDAO) o;
        return gameCounter == that.gameCounter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(memoryGames, gameCounter);
    }

    @Override
    public String toString() {
        return "MemoryGameDAO{" +
                "memoryGames=" + memoryGames +
                ", gameCounter=" + gameCounter +
                '}';
    }

}
