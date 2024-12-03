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
        server.connectWS();
    }

    public void run() {
        inGame = true;
        String username = "[Logged In]";
        System.out.print("\nLogged in!");
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
            server.redraw(gameID);
            GameListResult list = server.listGames();
            for (GameTemplateResult result : list.games()) {
                if (result.gameID() == gameID) {
                    ChessBoard board = result.game().getBoard();
                    if (Objects.equals(result.blackUsername(), username)) {
                        printBlackBoard(board);
                    } else {
                        printWhiteBoard(board);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to redraw board: "+e.getMessage());
        }
    }

    private void highlight() {
        try {
            GameListResult list = server.listGames();
            for (GameTemplateResult game : list.games()) {
                if (game.gameID() == gameID) {
                    //printBoard(new ChessBoard());
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to highlight valid moves: "+e.getMessage());
        }
    }

    private void move() {
        try {
            GameListResult list = server.listGames();
            for (GameTemplateResult game : list.games()) {
                if (game.gameID() == gameID) {
                    //printBoard(new ChessBoard());
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to logout: "+e.getMessage());
        }
    }

    private void resign() {
        try {
            GameListResult list = server.listGames();
            for (GameTemplateResult game : list.games()) {
                if (game.gameID() == gameID) {
                    //printBoard(new ChessBoard());
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to logout: "+e.getMessage());
        }
    }

    public void leave() {
        try {
            server.logout();
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

    private static final String SET_EDGE_COLOR = SET_BG_COLOR_DARK_RED;
    private void printBoard(GameData gameData, String username) {
        ChessBoard board = gameData.game().getBoard();
        if (Objects.equals(gameData.blackUsername(), username)) {
            printBlackBoard(board);
        } else {
            printWhiteBoard(board);
        }
    }
    private void printBlackBoard(ChessBoard board) {
        printEdgeRow("black");
        for (int row = 1; row <= 8; row++) {
            if (row % 2 == 1) {
                printRow(board,row,"white","back");
            } else {
                printRow(board,row,"black","back");
            }
        }
        printEdgeRow("black");
    }
    private void printWhiteBoard(ChessBoard board) {
        printEdgeRow("white");
        for (int row = 8; row >= 1; row--) {
            if (row % 2 == 0) {
                printRow(board,row,"white","norm");
            } else {
                printRow(board,row,"black","norm");
            }
        }
        printEdgeRow("white");
    }
    private void printRow(ChessBoard board, int rowNumber, String colorRow, String order) {
        printEdgeSpace(Integer.toString(rowNumber));
        if (order.equals("norm")){
            for (int column = 1; column <= 8; column ++) {
                if (column % 2 == 1) {
                    if (colorRow == "black") {
                        System.out.print(SET_BG_COLOR_DARK_GREY);
                    } else {
                        System.out.print(SET_BG_COLOR_LIGHT_GREY);
                    }
                } else {
                    if (colorRow == "white") {
                        System.out.print(SET_BG_COLOR_DARK_GREY);
                    } else {
                        System.out.print(SET_BG_COLOR_LIGHT_GREY);
                    }
                }
                printSpace(board.getPiece(new ChessPosition(rowNumber, column)));
            }
        }
        if (order.equals("back")){
            for (int column = 8; column >= 1; column--) {
                if (column % 2 == 1) {
                    if (colorRow == "black") {
                        System.out.print(SET_BG_COLOR_LIGHT_GREY);
                    } else {
                        System.out.print(SET_BG_COLOR_DARK_GREY);
                    }
                } else {
                    if (colorRow == "white") {
                        System.out.print(SET_BG_COLOR_LIGHT_GREY);
                    } else {
                        System.out.print(SET_BG_COLOR_DARK_GREY);
                    }
                }
                printSpace(board.getPiece(new ChessPosition(rowNumber, column)));
            }
        }
        printEdgeSpace(Integer.toString(rowNumber));
        System.out.println("");
    }

    private void printEdgeRow(String boardColor) {
        switch(boardColor) {
            case "black":
                printEdgeSpace(null);
                printEdgeSpace("h");
                printEdgeSpace("g");
                printEdgeSpace("f");
                printEdgeSpace("e");
                printEdgeSpace("d");
                printEdgeSpace("c");
                printEdgeSpace("b");
                printEdgeSpace("a");
                printEdgeSpace(null);
                break;

            case "white":
                printEdgeSpace(null);
                printEdgeSpace("a");
                printEdgeSpace("b");
                printEdgeSpace("c");
                printEdgeSpace("d");
                printEdgeSpace("e");
                printEdgeSpace("f");
                printEdgeSpace("g");
                printEdgeSpace("h");
                printEdgeSpace(null);
                break;
        }
        System.out.println("");
    }

    private void printSpace(ChessPiece space) {
        if (space == null) {
            System.out.print(EMPTY);
            return;
        }
        if (space.getTeamColor() == ChessGame.TeamColor.BLACK) {
            System.out.print(SET_TEXT_COLOR_BLACK);
        } else {
            System.out.print(SET_TEXT_COLOR_WHITE);
        }
        switch (space.getPieceType()) {
            case KING:
                System.out.print(BLACK_KING);
                break;
            case QUEEN:
                System.out.print(BLACK_QUEEN);
                break;
            case ROOK:
                System.out.print(BLACK_ROOK);
                break;
            case BISHOP:
                System.out.print(BLACK_BISHOP);
                break;
            case KNIGHT:
                System.out.print(BLACK_KNIGHT);
                break;
            case PAWN:
                System.out.print(BLACK_PAWN);
                break;
            case null, default:
                System.out.print(EMPTY);
                break;
        }
        System.out.print(RESET_TEXT_COLOR+RESET_BG_COLOR);
    }
    private void printEdgeSpace(String indicator) {
        System.out.print(SET_EDGE_COLOR);
        if (indicator == null) {
            System.out.print(THIN+" "+THIN);
        } else {
            System.out.print(THIN +indicator+THIN);
        }
        System.out.print(RESET_BG_COLOR);
    }

}
