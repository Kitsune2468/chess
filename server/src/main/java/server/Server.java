package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import handlers.DataBaseHandler;
import handlers.GameHandler;
import handlers.UserHandler;
import service.DataBaseService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {
    MemoryUserDAO userDAO = new MemoryUserDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    MemoryGameDAO gameDAO = new MemoryGameDAO();

    DataBaseService dataBaseService = new DataBaseService();
    UserService userService = new UserService();
    GameService gameService = new GameService();

    DataBaseHandler dataBaseHandler = new DataBaseHandler();
    UserHandler userHandler = new UserHandler();
    GameHandler gameHandler = new GameHandler();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (req, res) -> dataBaseHandler.clear(req, res));

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
