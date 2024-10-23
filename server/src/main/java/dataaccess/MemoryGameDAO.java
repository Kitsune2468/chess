package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {
    private ArrayList<GameData> memoryGames;

    public MemoryGameDAO() {
        memoryGames = new ArrayList<GameData>();
    }

    @Override
    public void addGame(GameData gameData) {
        memoryGames.add(gameData);
    }

    @Override
    public GameData getGameByString(String gameName) {
        GameData foundGame = null;
        for(GameData searchAuth : memoryGames) {
            if (searchAuth.gameName() == gameName) {
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
