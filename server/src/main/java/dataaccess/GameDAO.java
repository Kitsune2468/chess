package dataaccess;
import chess.ChessMove;
import model.GameData;
import model.requests.GameTemplateResult;

import java.util.ArrayList;

public interface GameDAO {
    public int addGame(String gameName) throws DataAccessException;

    public GameData getGameByString(String gameName) throws DataAccessException;

    public GameData getGameByID(int gameID) throws DataAccessException;

    public void joinGame(int gameID, String joinTeamColor, String playerName) throws DataAccessException;

    public ArrayList<GameTemplateResult> getAllGames() throws DataAccessException;

    public void clear() throws DataAccessException;

    public boolean isGameEmpty() throws DataAccessException;

    public GameData makeMove(String authToken, GameData gameData, ChessMove givenmove) throws Exception;

    public GameData resignGame(String authToken, int gameID, String resignTeamColor, String playerName) throws DataAccessException;
}
