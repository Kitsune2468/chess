package dataaccess;
import chess.ChessGame;
import model.GameData;
import service.Requests.GameTemplateResult;

import java.util.ArrayList;

public interface GameDAO {
    public int addGame(String gameName) throws DataAccessException;
    public GameData getGameByString(String gameName);
    public GameData getGameByID(int gameID);
    public void updateGame();
    public void joinGame(int gameID, String joinTeamColor, String playerName);
    public void deleteGameByString(String gameName);
    public ArrayList<GameTemplateResult> getAllGames();
    public void clear();
    public boolean isEmpty();
}
