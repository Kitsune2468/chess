package handlers;

import service.DataBaseService;
import service.GameService;

public class GameHandler {
    GameService gameService;
    public GameHandler(GameService inputGameService) {
        gameService = inputGameService;
    }
}
