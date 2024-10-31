package dataaccess;
import model.GameData;
import service.requests.GameTemplateResult;

import java.util.ArrayList;

public interface GameDAO {
    public int addGame(String gameName) throws DataAccessException;

    public GameData getGameByString(String gameName);

    public GameData getGameByID(int gameID);

    public void joinGame(int gameID, String joinTeamColor, String playerName);

    public ArrayList<GameTemplateResult> getAllGames();

    public void clear();

    public boolean isEmpty();
}
