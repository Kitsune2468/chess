package dataaccess;
import model.GameData;

public interface GameDAO {
    public void addGame(GameData gameData);
    public GameData getGameByString(String gameName);
    public void updateGame();
    public void deleteGameByString(String gameName);
    public void clear();
}
