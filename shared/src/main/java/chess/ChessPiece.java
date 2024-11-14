package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pColor;
    private ChessPiece.PieceType pType;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        pColor = pieceColor;
        pType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (board.getPiece(myPosition).getPieceType()) {
            case KING:
                return kingMoves(board, myPosition);
            case QUEEN:
                return queenMoves(board, myPosition);
            case BISHOP:
                return bishopMoves(board, myPosition);
            case ROOK:
                return rookMoves(board, myPosition);
            case KNIGHT:
                return knightMoves(board, myPosition);
            case PAWN:
                return pawnMoves(board, myPosition);
        }
        return new ArrayList<ChessMove>();
    }

    public ArrayList<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        for(int i = -1; i<=1; i++) {
            for(int j = -1; j<=1; j++) {
                int checkPosRow = startRow+i;
                int checkPosCol = startCol+j;

                // break if out of bounds
                if (checkPosRow<=0||checkPosRow>=9||checkPosCol<=0||checkPosCol>=9) {
                    continue;
                }
                ChessPiece checkPiece = board.getPiece(new ChessPosition(checkPosRow, checkPosCol));
                if (board.getPiece(new ChessPosition(checkPosRow, checkPosCol)) == null||checkPiece.getTeamColor() != pColor) {
                    possibleMoves.add(new ChessMove(myPosition,new ChessPosition(checkPosRow,checkPosCol),null));
                } else if (checkPiece.getTeamColor() == pColor) {
                    continue;
                }
            }
        }

        return possibleMoves;
    }

    public ArrayList<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        possibleMoves.addAll(bishopMoves(board, myPosition));
        possibleMoves.addAll(rookMoves(board, myPosition));

        return possibleMoves;
    }

    public ArrayList<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        //Check left-up diagonal
        int counter = 1;
        while(myPosition.getRow()+counter <= 8 && myPosition.getColumn()-counter >= 1) {
            int curRow = startRow + counter;
            int curCol = startCol - counter;

            if (board.getPiece(new ChessPosition(curRow, curCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        //Check right-up diagonal
        counter = 1;
        while(myPosition.getRow()+counter <= 8 && myPosition.getColumn()+counter <= 8) {
            int curRow = startRow + counter;
            int curCol = startCol + counter;

            if (board.getPiece(new ChessPosition(curRow, curCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        //Check right-down diagonal
        counter = 1;
        while(myPosition.getRow()-counter >= 1 && myPosition.getColumn()+counter <= 8) {
            int curRow = startRow - counter;
            int curCol = startCol + counter;

            if (board.getPiece(new ChessPosition(curRow, curCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        //Check left-down diagonal
        counter = 1;
        while(myPosition.getRow()-counter >= 1 && myPosition.getColumn()-counter >= 1) {
            int curRow = startRow - counter;
            int curCol = startCol - counter;

            if (board.getPiece(new ChessPosition(curRow, curCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        return possibleMoves;
    }

    public ArrayList<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        //Check Up
        int counter = 1;
        while(myPosition.getRow()+counter <= 8) {
            int curRow = startRow + counter;

            if (board.getPiece(new ChessPosition(curRow, startCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,startCol),null));
            } else if (board.getPiece(new ChessPosition(curRow, startCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,startCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(curRow, startCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        //Check Down
        counter = 1;
        while(myPosition.getRow()-counter >= 1) {
            int curRow = startRow - counter;

            if (board.getPiece(new ChessPosition(curRow, startCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,startCol),null));
            } else if (board.getPiece(new ChessPosition(curRow, startCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,startCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(curRow, startCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        //Check Right
        counter = 1;
        while(myPosition.getColumn()+counter <= 8) {
            int curCol = startCol + counter;

            if (board.getPiece(new ChessPosition(startRow, curCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(startRow,curCol),null));
            } else if (board.getPiece(new ChessPosition(startRow, curCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(startRow,curCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(startRow, curCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }
        //Check Left
        counter = 1;
        while(myPosition.getColumn()-counter >= 1) {
            int curCol = startCol - counter;

            if (board.getPiece(new ChessPosition(startRow, curCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(startRow,curCol),null));
            } else if (board.getPiece(new ChessPosition(startRow, curCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(startRow,curCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(startRow, curCol)).getTeamColor() == pColor) {
                break;
            }

            counter++;
        }

        return possibleMoves;
    }

    public ArrayList<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        int checkRow = startRow;
        int checkCol = startCol;
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        //Check up & down
        for (int i = -1; i <= 1; i = i+2) {
            for (int j = -1; j <= 1; j = j+2) {
                checkRow = startRow+(2*i);
                checkCol = startCol+j;
                ChessPosition checkPosition = new ChessPosition(checkRow,checkCol);
                if (validKnightMove(board,checkPosition)) {
                    possibleMoves.add(new ChessMove(myPosition,checkPosition,null));
                }
                checkRow = startRow+j;
                checkCol = startCol+(2*i);
                checkPosition = new ChessPosition(checkRow,checkCol);
                if (validKnightMove(board,checkPosition)) {
                    possibleMoves.add(new ChessMove(myPosition,checkPosition,null));
                }
            }
        }

        return possibleMoves;
    }
    private boolean validKnightMove(ChessBoard board, ChessPosition checkPosition) {
        if (checkPosition.getColumn()<1||checkPosition.getColumn()>8) {
            return false;
        }
        if (checkPosition.getRow()<1||checkPosition.getRow()>8) {
            return false;
        }
        ChessPiece checkPiece = board.getPiece(checkPosition);
        if (checkPiece != null) {
            if (checkPiece.getTeamColor() == pColor) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        int checkRow;
        int checkCol;
        boolean onStartRow = false;
        boolean blocked = false;
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int baseOffset = 0;
        int promotionRow = 0;
        ChessPiece startPiece = board.getPiece(new ChessPosition(startRow, startCol));
        ChessPiece checkPiece = null;

        if (startPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            //If black pawn, front is down
            baseOffset = -1;
            promotionRow = 1;
            if (startRow == 7) {
                onStartRow = true;
            }
        } else {
            //Else, is white pawn and front is up
            baseOffset = 1;
            promotionRow = 8;
            if (startRow == 2) {
                onStartRow = true;
            }
        }

        //Forward 1
        checkRow = startRow+baseOffset;
        if (checkRow<1||checkRow>8) {
            return possibleMoves;
        }
        checkCol = startCol;
        ChessPosition checkPosition = new ChessPosition(checkRow, checkCol);
        checkPiece = board.getPiece(checkPosition);
        if (checkPiece==null) {
            if (checkRow==promotionRow) {
                possibleMoves.addAll(promotePawn(myPosition,checkPosition));
            } else {
                possibleMoves.add(new ChessMove(myPosition,checkPosition,null));
            }
            //Forward 2 if on start row
            if (onStartRow) {
                checkRow = checkRow+baseOffset;
                checkPosition = new ChessPosition(checkRow, checkCol);
                if (board.getPiece(checkPosition) == null) {
                    possibleMoves.add(new ChessMove(myPosition,checkPosition,null));
                }
                checkRow = startRow+baseOffset;
            }
        }

        //Base Logic to the sides
        checkCol = startCol-1;
        checkPosition = new ChessPosition(checkRow, checkCol);
        if (checkCol>=1&&checkCol<=8) {
            checkPiece = board.getPiece(checkPosition);
            if (checkPiece != null && checkPiece.getTeamColor() != pColor) {
                if (checkRow==promotionRow) {
                    possibleMoves.addAll(promotePawn(myPosition,checkPosition));
                } else {
                    possibleMoves.add(new ChessMove(myPosition,checkPosition,null));
                }
            }
        }
        checkCol = startCol+1;
        checkPosition = new ChessPosition(checkRow, checkCol);
        if (checkCol>=1&&checkCol<=8) {
            checkPiece = board.getPiece(checkPosition);
            if (checkPiece != null && checkPiece.getTeamColor() != pColor) {
                if (checkRow == promotionRow) {
                    possibleMoves.addAll(promotePawn(myPosition, checkPosition));
                } else {
                    possibleMoves.add(new ChessMove(myPosition, checkPosition, null));
                }
            }
        }

        return possibleMoves;
    }
    public ArrayList<ChessMove> promotePawn(ChessPosition myPosition,ChessPosition checkPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        possibleMoves.add(new ChessMove(myPosition,checkPosition,PieceType.QUEEN));
        possibleMoves.add(new ChessMove(myPosition,checkPosition,PieceType.BISHOP));
        possibleMoves.add(new ChessMove(myPosition,checkPosition,PieceType.ROOK));
        possibleMoves.add(new ChessMove(myPosition,checkPosition,PieceType.KNIGHT));

        return possibleMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        ChessPiece that = (ChessPiece) o;
        return pColor == that.pColor && pType == that.pType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pColor, pType);
    }
}
