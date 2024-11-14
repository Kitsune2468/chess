package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import client.ServerFacade;
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
            System.out.println("\nCurrent games: ");
            int gameCounter = 1;
            listOfGames = new HashMap<>();
            for (var game:currentGames.games()) {
                System.out.print(" "+gameCounter+" - ");
                listOfGames.put(gameCounter,game);
                printGame(game);
                gameCounter++;
            }
            System.out.println("");
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
                gameID = listOfGames.get(gameNumber).gameID();
            } catch (NumberFormatException e) {
                System.out.println("Invalid game number");
                return;
            }

            System.out.print("Do you want to join the white or black team?\n   ");
            line = scanner.nextLine();
            String teamToJoin = line.strip().toLowerCase();
            if (teamToJoin.equals("black")) {
                teamToJoin = "BLACK";
            } else if (teamToJoin.equals("white")){
                teamToJoin = "WHITE";
            } else {
                System.out.println("Invalid team color");
                return;
            }

            server.joinGame(gameID,teamToJoin);
        } catch (Exception e) {
            System.out.println("Failed to join game.");
        }
    }

    public void observe() {
        try {
            list();

            System.out.print("Enter the number for the game you want to join:\n   ");
            String line = scanner.nextLine();
            int gameID = 0;
            int gameNumber = 0;
            try {
                gameNumber = Integer.parseInt(line.strip());
                gameID = listOfGames.get(gameNumber).gameID();
                printBoard(new ChessGame().getBoard());
            } catch (NumberFormatException e) {
                System.out.println("Invalid game number");
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
            printBoard(new ChessGame().getBoard());
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

    private static String SET_EDGE_COLOR = SET_BG_COLOR_DARK_RED;
    private void printBoard(ChessBoard board) {
        printWhiteBoard(board);
        printBlackBoard(board);
    }
    private void printBlackBoard(ChessBoard board) {
        printEdgeRow("black");
        for (int row = 1; row <= 8; row++) {
            if (row % 2 == 1) {
                printWhiteRow(board,row,1);
            } else {
                printBlackRow(board,row,1);
            }
        }
        printEdgeRow("black");
    }
    private void printWhiteBoard(ChessBoard board) {
        printEdgeRow("white");
        for (int row = 8; row >= 1; row--) {
            if (row % 2 == 0) {
                printWhiteRow(board,row,1);
            } else {
                printBlackRow(board,row,1);
            }
        }
        printEdgeRow("white");
    }
    private void printWhiteRow(ChessBoard board, int rowNumber, int rowOrientation) {
        printEdgeSpace(Integer.toString(rowNumber));
        for (int column = 1; column <= 8; column ++) {
            if (column % 2 == 1) {
                System.out.print(SET_BG_COLOR_LIGHT_GREY);
            } else {
                System.out.print(SET_BG_COLOR_DARK_GREY);
            }
            printSpace(board.getPiece(new ChessPosition(rowNumber,column)));
        }
        printEdgeSpace(Integer.toString(rowNumber));
        System.out.println("");
    }
    private void printBlackRow(ChessBoard board, int rowNumber, int rowOrientation) {
        printEdgeSpace(Integer.toString(rowNumber));
        for (int column = 1; column <= 8; column ++) {
            if (column % 2 == 1) {
                System.out.print(SET_BG_COLOR_DARK_GREY);
            } else {
                System.out.print(SET_BG_COLOR_LIGHT_GREY);
            }
            printSpace(board.getPiece(new ChessPosition(rowNumber,column)));
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
