package server;

import dataaccess.*;
import handlers.DataBaseHandler;
import handlers.GameHandler;
import handlers.UserHandler;
import handlers.WebSocketHandler;
import org.eclipse.jetty.websocket.api.Session;
import service.DataBaseService;
import service.GameService;
import service.UserService;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.*;

import java.util.HashMap;

public class Server {
    private final WebSocketHandler webSocketHandler;

    public UserDAO userDAO;
    public AuthDAO authDAO;
    public GameDAO gameDAO;

    DataBaseService dataBaseService;
    GameService gameService;
    UserService userService;


    DataBaseHandler dataBaseHandler;
    GameHandler gameHandler;
    UserHandler userHandler;

    public Server() {
        webSocketHandler = new WebSocketHandler(this);

        try {
        userDAO = new SQLUserDAO();
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();

        dataBaseService = new DataBaseService(authDAO, gameDAO, userDAO);
        gameService = new GameService(gameDAO, authDAO);
        userService = new UserService(userDAO, authDAO);

        dataBaseHandler = new DataBaseHandler(dataBaseService);
        gameHandler = new GameHandler(gameService);
        userHandler = new UserHandler(userService);

        } catch (Exception e) {
            System.out.println("Failed to initialize Server class" + e.getMessage());
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", webSocketHandler);

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (req, res) -> (dataBaseHandler).clear(req, res));
        Spark.post("/user", (req, res) -> (userHandler).addUser(req, res));
        Spark.post("/session", (req, res) -> (userHandler).login(req, res));
        Spark.delete("/session", (req, res) -> (userHandler).logout(req, res));
        Spark.post("/game", (req, res) -> (gameHandler).createGame(req, res));
        Spark.get("/game", (req, res) -> (gameHandler).listGames(req, res));
        Spark.put("/game", (req, res) -> (gameHandler).joinGame(req, res));


        //This line initializes the server and can be removed once you have a functioning endpoint 
        //Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
