package ui;

import client.ServerFacade;
import dataaccess.DataAccessException;
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
                    create();
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
            //ArrayList<GameTemplateResult> currentGames = server.listGames();

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

    public void create() {
        try {
            //ArrayList<GameTemplateResult> currentGames = server.listGames();

        } catch (Exception e) {
            System.out.println("IDK, error or something lol");
        }
    }

    public void logout() {
        try {
            server.logout();
            System.out.println("\nLogging out...");
            loggedIn = false;
        } catch (Exception e) {
            System.out.println("IDK, error or something lol");
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

}
