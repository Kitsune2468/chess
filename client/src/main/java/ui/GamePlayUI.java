package ui;

import chess.*;
import client.ServerFacade;
import model.GameData;
import model.requests.GameListResult;
import model.requests.GameTemplateResult;
import websocket.commands.UserGameCommand;

import java.util.*;

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
    String team;
    BoardPrinter printer = new BoardPrinter();

    public GamePlayUI(ServerFacade serverFacade, GameData refGameData, boolean isObserving, String username) throws Exception {
        server = serverFacade;
        gameData = refGameData;
        gameID = refGameData.gameID();
        team = checkTeam(gameData);
        observing = isObserving;
        this.username = username;
        server.connectGameplayUI(this);
        server.connectWS(username);
    }

    public void run() throws InterruptedException {
        inGame = true;
        server.sendConnect(gameID);
        if (!observing) {
            team = checkTeam(gameData);
            if (team == null) {
                System.out.print("Do you want to join the white or black team?\n   ");
                String line = scanner.nextLine();
                String teamToJoin = line.strip().toLowerCase();
                if (teamToJoin.equals("black")) {
                    teamToJoin = "BLACK";
                } else if (teamToJoin.equals("white")) {
                    teamToJoin = "WHITE";
                } else {
                    System.out.println("Invalid team color");
                    leave();
                }
                try {
                    server.joinGame(gameID, teamToJoin);
                } catch (Exception e) {
                    System.out.println("Unable to join game, returning to main menu...");
                    leave();
                }
            }
        } else {
            help();
        }

        String username = "[Game - \""+gameData.gameName()+"\"]";
        System.out.println("Entered Game!");

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
                    if (!gameData.gameActive()) {
                        System.out.println("Game has ended, no moves can be made.");
                        break;
                    }
                    if (observing) {
                        System.out.println("You are observing, and cannot make moves.");
                        break;
                    }
                    move();
                    break;

                case "resign":
                    if (!gameData.gameActive()) {
                        System.out.println("Game has ended, you cannot resign.");
                        break;
                    }
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
            printer.printGame(gameData);
            server.sendRedraw(gameID);
        } catch (Exception e) {
            System.out.println("Failed to redraw board: "+e.getMessage());
        }
    }

    private void highlight() {
        try {
            ChessPosition startPosition = getChessPosition("piece");
            server.sendHighlight(gameID,startPosition);
        } catch (Exception e) {
            System.out.println("Failed to highlight valid moves: "+e.getMessage());
        }
    }

    private void move() {
        try {
            server.sendRedraw(gameID);
            team = checkTeam(gameData);
            ChessGame.TeamColor checkColor = null;
            if (Objects.equals(team, "white")) {
                checkColor = ChessGame.TeamColor.WHITE;
            }
            if (Objects.equals(team, "black")) {
                checkColor = ChessGame.TeamColor.BLACK;
            }
            if (!Objects.equals(checkColor, gameData.game().getTeamTurn())) {
                System.out.println("It is not your turn. Please wait for the other player to make their move.");
                return;
            }
            ChessPosition startPosition = getChessPosition("piece");
            server.sendHighlight(gameID,startPosition);
            ChessPosition endPosition = getChessPosition("destination");
            ChessPiece.PieceType promoPiece;
            if (endPosition.getRow()==1 && team=="black") {
                promoPiece = getPromotionPiece();
            } else if (endPosition.getRow()==8 && team=="white") {
                promoPiece = getPromotionPiece();
            } else {
                promoPiece = null;
            }
            ChessMove move = new ChessMove(startPosition,endPosition,promoPiece);
            server.sendMakeMove(gameID, move);

        } catch (Exception e) {
            System.out.println("Failed to make move: "+e.getMessage());
        }
    }

    private void resign() {
        try {
            server.sendResign(gameID);
        } catch (Exception e) {
            System.out.println("Failed to resign: "+e.getMessage());
        }
    }

    public void leave() {
        try {
            server.sendLeaveSession(gameID);
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
        System.out.println(SET_MENU_OPTION+"leave"+RESET_TEXT_COLOR+" - Leaves the game and returns to main menu");
    }



    private ChessPosition getChessPosition(String positionType) {
        System.out.print("Enter the column and row of the "+positionType+" (Ex: a4, e7, etc.):\n   ");
        String line = scanner.nextLine();
        if (line.length() != 2) {
            System.out.println("Invalid coordinate, returning to game menu...");
            return null;
        }
        String column = line.strip().substring(0,1);
        int colNum;
        switch (column) {
            case "a":
                colNum = 1;
                break;
            case "b":
                colNum = 2;
                break;
            case "c":
                colNum = 3;
                break;
            case "d":
                colNum = 4;
                break;
            case "e":
                colNum = 5;
                break;
            case "f":
                colNum = 6;
                break;
            case "g":
                colNum = 7;
                break;
            case "h":
                colNum = 8;
                break;
            case null, default:
                System.out.println("Invalid coordinate, returning to game menu...");
                return null;
        }
        int rowNum = Integer.parseInt(line.strip().substring(1,2));
        ChessPosition position = new ChessPosition(rowNum, colNum);
        return position;
    }

    private ChessPiece.PieceType getPromotionPiece() {
        System.out.println("What piece do you want to promote to?");
        System.out.println("(Queen/Knight/Bishop/Rook)");
        String line = scanner.nextLine();
        String column = line.strip().toLowerCase();
        ChessPiece.PieceType promoPiece;
        switch (column) {
            case "queen":
                promoPiece = ChessPiece.PieceType.QUEEN;
                break;
            case "knight":
                promoPiece = ChessPiece.PieceType.KNIGHT;
                break;
            case "bishop":
                promoPiece = ChessPiece.PieceType.BISHOP;
                break;
            case "rook":
                promoPiece = ChessPiece.PieceType.ROOK;
                break;
            case null, default:
                System.out.println("Invalid coordinate, returning to game menu...");
                return null;
        }
        return promoPiece;
    }

    private String checkTeam(GameData gameData) {
        String foundTeam = null;
        if (gameData.whiteUsername() != null) {
            if (gameData.whiteUsername().equals(username)) {
                foundTeam = "white";
            }
        }
        if (gameData.blackUsername() != null) {
            if (gameData.blackUsername().equals(username)) {
                foundTeam = "black";
            }
        }
        return foundTeam;
    }

    public void setGameDataForUI(GameData gameData) {
        this.gameData = gameData;
    }
}