package ui;

import chess.*;
import model.GameData;

import java.util.*;

import static ui.EscapeSequences.*;

public class BoardPrinter {

    private static final String SET_EDGE_COLOR = SET_BG_COLOR_DARK_RED;
    public void printBoard(GameData gameData, String username, Collection<ChessMove> possibleMoves) {
        ChessBoard board = gameData.game().getBoard();
        System.out.println("");
        if (Objects.equals(gameData.blackUsername(), username)) {
            printBlackBoard(board, possibleMoves);
        } else {
            printWhiteBoard(board, possibleMoves);
        }
    }
    public void printBoard(GameData gameData, String username) {
        Collection<ChessMove> nullMoves = Collections.emptyList();
        printBoard(gameData, username, nullMoves);
    }
    private void printBlackBoard(ChessBoard board,Collection<ChessMove> possibleMoves) {
        printEdgeRow("black");
        for (int row = 1; row <= 8; row++) {
            printRow(board,row,"black", possibleMoves);
        }
        printEdgeRow("black");
    }
    private void printWhiteBoard(ChessBoard board, Collection<ChessMove> possibleMoves) {
        printEdgeRow("white");
        for (int row = 8; row >= 1; row--) {
            printRow(board,row,"white", possibleMoves);
        }
        printEdgeRow("white");
    }
    private void printRow(ChessBoard board, int rowNumber, String colorBoard, Collection<ChessMove> possibleMoves) {
        printEdgeSpace(Integer.toString(rowNumber));
        for (int col = 0; col < 8; col++) {
            setBackColor(rowNumber, col+1, colorBoard, possibleMoves);
            printSpace(board.getPiece(new ChessPosition(rowNumber, col+1)));
        }
        printEdgeSpace(Integer.toString(rowNumber));
        System.out.println("");
    }


    private void setBackColor(int rowNumber, int colNumber, String colorBoard, Collection<ChessMove> possibleMoves) {
        int perspectiveRow;
        if (colorBoard.equals("white")) {
            perspectiveRow = 9-rowNumber;
        } else {
            perspectiveRow = rowNumber;
        }
        if (perspectiveRow % 2 == colNumber % 2) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY);
        } else {
            System.out.print(SET_BG_COLOR_DARK_GREY);
        }
        ChessPosition checkPosition = new ChessPosition(rowNumber,colNumber);
        if (possibleMoves.isEmpty()) {
            return;
        }
        for (ChessMove move : possibleMoves) {
            if (Objects.equals(checkPosition, move.getEndPosition())) {
                if (perspectiveRow % 2 == colNumber % 2) {
                    System.out.print(SET_BG_COLOR_GREEN2);
                } else {
                    System.out.print(SET_BG_COLOR_GREEN1);
                }
            }
            if (Objects.equals(checkPosition, move.getStartPosition())) {
                System.out.print(SET_BG_COLOR_RED);
            }
        }
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

    public void resetConsole() {
        System.out.println(ERASE_SCREEN);
    }
}
