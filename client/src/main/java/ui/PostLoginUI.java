package ui;

import client.ServerFacade;
import dataaccess.DataAccessException;
import service.requests.GameListResult;
import service.requests.GameTemplateResult;

import java.util.ArrayList;
import java.util.Scanner;

public class PostLoginUI {
    ServerFacade server;
    Scanner scanner = new Scanner(System.in);
    PreLoginUI preLoginUI;
    boolean loggedIn = true;

    public PostLoginUI(ServerFacade serverFacade, PreLoginUI referencePreLoginUI) {
        server = serverFacade;
        preLoginUI = referencePreLoginUI;
    }

    public void run() {
        loggedIn = true;
        String username = "[Logged In]";
        System.out.print("\nLogged in!");
        help();
        while(loggedIn) {
            System.out.print(username+" >>> ");
            String line = scanner.nextLine();
            switch (line) {
                case "list":
                    list();
                    break;

                case "play":
                    play();
                    break;

                case "observe":
                    observe();
                    break;

                case "create":
                    createGame();
                    break;

                case "help":
                    help();
                    break;

                case "logout":
                    logout();
                    break;

                case null, default:
                    System.out.println("Invalid command, please try again. (Type help to display available commands.)\n");
                    break;
            }
        }
    }

    public void list() {
        try {
            GameListResult currentGames = server.listGames();
            System.out.println("Current games: ");
            int gameCounter = 1;
            for (var game:currentGames.games()) {
                System.out.print(gameCounter+": ");
                printGame(game);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void play() {
        try {
            //server.joinGame();
        } catch (Exception e) {
            System.out.println("IDK, error or something lol");
        }
    }

    public void observe() {
        try {
            //ArrayList<GameTemplateResult> currentGames = server.listGames();

        } catch (Exception e) {
            System.out.println("IDK, error or something lol");
        }
    }

    public void createGame() {
        try {
            String gameName;
            System.out.println("Creating game:");
            System.out.print("Enter the name of the game you want to create:\n   ");
            String line = scanner.nextLine();
            gameName = line.strip();
            server.createGame(gameName);
            System.out.println("Created new game: "+gameName);
        } catch (Exception e) {
            System.out.println("Error in createGame: "+e.getMessage());
        }
    }

    public void logout() {
        try {
            server.logout();
            System.out.println("\nLogging out...");
            loggedIn = false;
        } catch (Exception e) {
            System.out.println("Failed to logout: "+e.getMessage());
        }
    }

    public void help() {
        System.out.println("\nHere are your available commands: ");
        System.out.println("list - Lists all current chess games");
        System.out.println("play - Join a chess game");
        System.out.println("observe - Observe a current chess game");
        System.out.println("create - Create a new chess game");
        System.out.println("help - Displays the available commands");
        System.out.println("logout - Logs out and returns to login menu\n");
    }

    private void printGame(GameTemplateResult game) {
        String gameName = game.gameName();
        String blackUser = game.blackUsername();
        if (blackUser == null) {
            blackUser = "None";
        }
        String whiteUser = game.whiteUsername();
        if (whiteUser == null) {
            whiteUser = "None";
        }
        System.out.printf("Game Name: %-10s Black: %-10s White: %-10s\n",gameName,blackUser,whiteUser);
    }

}
