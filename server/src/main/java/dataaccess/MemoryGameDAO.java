package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {
    private ArrayList<GameData> memoryGames;

    @Override
    public void addGame(GameData gameData) {
        memoryGames.add(gameData);
    }

    @Override
    public GameData getGameByString(String gameName) {
        return null;
    }

    @Override
    public void updateGame() {

    }

    @Override
    public void deleteGameByString(String gameName) {

    }

    @Override
    public void clearGames() {
        memoryGames.clear();
    }
}