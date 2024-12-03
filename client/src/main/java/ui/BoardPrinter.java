package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

import java.util.Objects;

import static ui.EscapeSequences.*;

public class BoardPrinter {

    private static final String SET_EDGE_COLOR = SET_BG_COLOR_DARK_RED;
    public void printBoard(GameData gameData, String username) {
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
