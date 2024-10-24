package server;

import dataaccess.*;
import handlers.DataBaseHandler;
import handlers.GameHandler;
import handlers.UserHandler;
import service.DataBaseService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    DataBaseService dataBaseService;
    GameService gameService;
    UserService userService;


    DataBaseHandler dataBaseHandler;
    GameHandler gameHandler;
    UserHandler userHandler;

    public Server() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();

        dataBaseService = new DataBaseService(authDAO, gameDAO, userDAO);
        gameService = new GameService(gameDAO,authDAO);
        userService = new UserService(userDAO,authDAO);

        dataBaseHandler = new DataBaseHandler(dataBaseService);
        gameHandler = new GameHandler(gameService);
        userHandler = new UserHandler(userService);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (req, res) -> (dataBaseHandler).clear(req, res));
        Spark.post("/user", (req, res) -> (userHandler).addUser(req, res));
        Spark.post("/session", (req, res) -> (userHandler).login(req, res));
        Spark.delete("/session", (req, res) -> (userHandler).logout(req, res));


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
