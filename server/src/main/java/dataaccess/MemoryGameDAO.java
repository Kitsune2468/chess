package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.UUID;

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
        gameCounter++;
        return 0;
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
    public void updateGame() {

    }

    @Override
    public void deleteGameByString(String gameName) {

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
}
