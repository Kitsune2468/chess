package dataaccess;
import model.GameData;

public interface GameDAO {
    public int addGame(String gameName) throws DataAccessException;
    public GameData getGameByString(String gameName);
    public void updateGame();
    public void deleteGameByString(String gameName);
    public void clear();
    public boolean isEmpty();
}
