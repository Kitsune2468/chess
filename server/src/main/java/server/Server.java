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
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    DataBaseService dataBaseService;
    GameService gameService;
    UserService userService;


    DataBaseHandler dataBaseHandler;
    GameHandler gameHandler;
    UserHandler userHandler;

    WebSocketHandler webSocketHandler;
    public HashMap<Session, Integer> currentGameSessions = new HashMap<>();

    public Server() {
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

            webSocketHandler = new WebSocketHandler(this);
        } catch (DataAccessException e) {
            System.out.println("Failed to initialize Server class" + e.getMessage());
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (req, res) -> (dataBaseHandler).clear(req, res));
        Spark.post("/user", (req, res) -> (userHandler).addUser(req, res));
        Spark.post("/session", (req, res) -> (userHandler).login(req, res));
        Spark.delete("/session", (req, res) -> (userHandler).logout(req, res));
        Spark.post("/game", (req, res) -> (gameHandler).createGame(req, res));
        Spark.get("/game", (req, res) -> (gameHandler).listGames(req, res));
        Spark.put("/game", (req, res) -> (gameHandler).joinGame(req, res));
        Spark.webSocket("/ws", WebSocketHandler.class);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        currentGameSessions.clear();
        Spark.stop();
        Spark.awaitStop();
    }
}
