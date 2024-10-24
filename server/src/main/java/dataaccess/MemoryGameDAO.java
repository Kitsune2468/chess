package dataaccess;

import chess.ChessGame;
import model.GameData;
import service.requests.GameTemplateResult;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
    private ArrayList<GameData> memoryGames;
    private int gameCounter;

    public MemoryGameDAO() {
        memoryGames = new ArrayList<GameData>();
        gameCounter = 1;
    }

    @Override
    public int addGame(String gameName) {
        GameData newGame = new GameData(gameCounter,null, null, gameName, new ChessGame());
        memoryGames.add(newGame);
        return gameCounter++;
    }

    @Override
    public GameData getGameByID(int gameID) {
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
    public GameData getGameByString(String gameName) {
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
    public void joinGame(int gameID, String joinTeamColor, String playerName) {
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
    public ArrayList<GameTemplateResult> getAllGames() {
        ArrayList<GameTemplateResult> games = new ArrayList<GameTemplateResult>();
        for (GameData game : memoryGames) {
            GameTemplateResult convertedGame = new GameTemplateResult(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName());
            games.add(convertedGame);
        }
        return games;
    }

    @Override
    public void clear() {
        memoryGames.clear();
    }

    @Override
    public boolean isEmpty() {
        if (memoryGames.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        MemoryGameDAO that = (MemoryGameDAO) o;
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
