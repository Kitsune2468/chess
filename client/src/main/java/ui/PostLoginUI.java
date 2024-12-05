package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import client.ServerFacade;
import model.GameData;
import model.requests.GameListResult;
import model.requests.GameTemplateResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class PostLoginUI {
    ServerFacade server;
    Scanner scanner = new Scanner(System.in);
    PreLoginUI preLoginUI;
    boolean loggedIn = true;
    Map<Integer,GameTemplateResult> listOfGames = new HashMap<>();
    String username;
    BoardPrinter printer = new BoardPrinter();

    public PostLoginUI(ServerFacade serverFacade, PreLoginUI referencePreLoginUI, String username) {
        server = serverFacade;
        preLoginUI = referencePreLoginUI;
        this.username = username;

    }

    public void run() {
        loggedIn = true;
        String username = "[Logged In]";
        System.out.print("\nLogged in!");
        help();
        while(loggedIn) {
            printer.resetConsole();
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
            listOfGames = new HashMap<>();
            for (var game:currentGames.games()) {
                System.out.print(" "+gameCounter+" - ");
                listOfGames.put(gameCounter,game);
                printer.printGame(game);
                gameCounter++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void play() {
        try {
            list();

            System.out.print("Enter the number for the game you want to join:\n   ");
            String line = scanner.nextLine();
            int gameID = 0;
            try {
                int gameNumber = Integer.parseInt(line.strip());
                GameTemplateResult resultGame = listOfGames.get(gameNumber);
                gameID = resultGame.gameID();
                String gameName = resultGame.gameName();
                String whiteUsername = resultGame.whiteUsername();
                String blackUsername = resultGame.blackUsername();
                ChessGame chessGame = resultGame.game();
                boolean gameActive = resultGame.gameActive();
                GameData game = new GameData(gameID,gameName,whiteUsername,blackUsername,chessGame, gameActive);
                GamePlayUI gamePlayUI = new GamePlayUI(server, game, false,username);
                gamePlayUI.run();
            } catch (NumberFormatException e) {
                System.out.println("Invalid game number");
            }
        } catch (Exception e) {
            System.out.println("Failed to join game.");
        }
    }

    public void observe() {
        try {
            list();

            System.out.print("Enter the number for the game you want to join:\n   ");
            String line = scanner.nextLine();
            int gameNumber = 0;
            try {
                gameNumber = Integer.parseInt(line.strip());
                GameTemplateResult resultGame = listOfGames.get(gameNumber);
                int gameID = resultGame.gameID();
                String gameName = resultGame.gameName();
                String whiteUsername = resultGame.whiteUsername();
                String blackUsername = resultGame.blackUsername();
                ChessGame chessGame = resultGame.game();
                boolean gameActive = resultGame.gameActive();
                GameData game = new GameData(gameID,gameName,whiteUsername,blackUsername,chessGame,gameActive);
                GamePlayUI gamePlayUI = new GamePlayUI(server, game, true,username);
                gamePlayUI.run();
            } catch (Exception e) {
                System.out.println("Invalid game number: "+e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Failed to observe game");
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

    private static final String SET_MENU_OPTION = SET_TEXT_COLOR_GREEN;
    public void help() {
        System.out.println("\nHere are your available commands: ");
        System.out.println(SET_MENU_OPTION+"list"+RESET_TEXT_COLOR+" - Lists all current chess games");
        System.out.println(SET_MENU_OPTION+"play"+RESET_TEXT_COLOR+" - Join a chess game");
        System.out.println(SET_MENU_OPTION+"observe"+RESET_TEXT_COLOR+" - Observe a current chess game");
        System.out.println(SET_MENU_OPTION+"create"+RESET_TEXT_COLOR+" - Create a new chess game");
        System.out.println(SET_MENU_OPTION+"help"+RESET_TEXT_COLOR+" - Displays the available commands");
        System.out.println(SET_MENU_OPTION+"logout"+RESET_TEXT_COLOR+" - Logs out and returns to login menu");
    }



}
