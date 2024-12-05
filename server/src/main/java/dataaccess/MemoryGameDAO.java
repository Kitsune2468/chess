package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import model.GameData;
import model.requests.GameTemplateResult;

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
    public int addGame(String gameName) throws DataAccessException{
        if (getGameByString(gameName) != null) {
            throw new DataAccessException("bad request");
        }
        GameData newGame = new GameData(gameCounter,gameName,null, null, new ChessGame(), true);
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
            if (foundGame == null) {
                return null;
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
                            searchGame.gameName(),
                            searchGame.whiteUsername(),
                            playerName,
                            searchGame.game(),
                            searchGame.gameActive());
                    memoryGames.remove(searchGame);
                    memoryGames.add(updatedGame);
                } else if (joinTeamColor.equals("WHITE")) {
                    GameData updatedGame = new GameData(searchGame.gameID(),
                            searchGame.gameName(),
                            playerName,
                            searchGame.blackUsername(),
                            searchGame.game(),
                            searchGame.gameActive());
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
            int gameID = game.gameID();
            String wUserName = game.whiteUsername();
            String bUserName = game.blackUsername();
            String gameName = game.gameName();
            ChessGame chessGame = game.game();
            boolean gameActive = game.gameActive();

            GameTemplateResult convertedGame = new GameTemplateResult(gameID,wUserName,bUserName,gameName,chessGame,gameActive);
            games.add(convertedGame);
        }
        return games;
    }

    @Override
    public void clear() {
        memoryGames.clear();
    }

    @Override
    public boolean isGameEmpty() {
        if (memoryGames.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public GameData makeMove(String auth, GameData gameData, ChessMove givenMove) throws DataAccessException {
        return gameData;
    }

    @Override
    public GameData resignGame(String authToken, int gameID, String resignTeamColor, String playerName) throws DataAccessException {
        return null;
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
