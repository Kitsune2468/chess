package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import client.ServerFacade;
import model.GameData;
import model.requests.GameListResult;
import model.requests.GameTemplateResult;
import websocket.commands.UserGameCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class GamePlayUI{
    ServerFacade server;
    Scanner scanner = new Scanner(System.in);
    boolean inGame = true;
    Map<Integer, GameTemplateResult> listOfGames = new HashMap<>();
    GameData gameData;
    int gameID;
    boolean observing;
    String username;

    public GamePlayUI(ServerFacade serverFacade, GameData refGameData, boolean isObserving, String username) throws Exception {
        server = serverFacade;
        gameData = refGameData;
        gameID = refGameData.gameID();
        observing = isObserving;
        this.username = username;
        server.connectWS(username);
    }

    public void run() {
        inGame = true;
        String username = "[Game]";
        System.out.print("\nEntered Game!");
        help();
        while(inGame) {
            System.out.print(username+" >>> ");
            String line = scanner.nextLine();
            switch (line) {
                case "redraw":
                    redraw();
                    break;

                case "highlight":
                    highlight();
                    break;

                case "move":
                    if (observing) {
                        System.out.println("You are observing, and cannot make moves.");
                        break;
                    }
                    move();
                    break;

                case "resign":
                    if (observing) {
                        System.out.println("You are observing, and cannot resign.");
                        break;
                    }
                    resign();
                    break;

                case "help":
                    help();
                    break;

                case "leave":
                    leave();
                    break;

                case null, default:
                    System.out.println("Invalid command, please try again. (Type help to display available commands.)\n");
                    break;
            }
        }
    }

    private void redraw() {
        try {
            server.sendRedraw(gameID);
        } catch (Exception e) {
            System.out.println("Failed to redraw board: "+e.getMessage());
        }
    }

    private void highlight() {
        try {

        } catch (Exception e) {
            System.out.println("Failed to highlight valid moves: "+e.getMessage());
        }
    }

    private void move() {
        try {

        } catch (Exception e) {
            System.out.println("Failed to logout: "+e.getMessage());
        }
    }

    private void resign() {
        try {

        } catch (Exception e) {
            System.out.println("Failed to logout: "+e.getMessage());
        }
    }

    public void leave() {
        try {
            // TODO: leave websocket command here
            System.out.println("\nReturning to main menu...");
            inGame = false;
        } catch (Exception e) {
            System.out.println("Failed to logout: "+e.getMessage());
        }
    }

    private static final String SET_MENU_OPTION = SET_TEXT_COLOR_GREEN;
    public void help() {
        System.out.println("\nHere are your available commands: ");
        System.out.println(SET_MENU_OPTION+"redraw"+RESET_TEXT_COLOR+" - Redraws current game");
        System.out.println(SET_MENU_OPTION+"highlight"+RESET_TEXT_COLOR+" - Highlight the legal moves for a piece");
        if (!observing) {
            System.out.println(SET_MENU_OPTION + "move" + RESET_TEXT_COLOR + " - Make a move");
            System.out.println(SET_MENU_OPTION + "resign" + RESET_TEXT_COLOR + " - Resign from the game");
        }
        System.out.println(SET_MENU_OPTION+"help"+RESET_TEXT_COLOR+" - Displays the available commands");
        System.out.println(SET_MENU_OPTION+"leave"+RESET_TEXT_COLOR+" - Leaves the game and returns to main menu\n");
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
